package com.lie.puremusic.fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.king.view.arcseekbar.ArcSeekBar
import com.lie.puremusic.*
import com.lie.puremusic.activity.PlayerActivity
import com.lie.puremusic.adapter.MediaPlayerHelper
import com.lie.puremusic.databinding.FragmentPlayerBinding
import com.lie.puremusic.utils.BurnUtil
import com.lie.puremusic.utils.GetLyricData
import com.lie.puremusic.utils.GetMusicData
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import java.io.File
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PlayerFragment : Fragment(), View.OnClickListener{
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private var animation: Animation? = null
    private var isSeekbarChaning = false
    private var CanPlay = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FileDownloader.setup(context)
        binding.Cover.setOnClickListener(this)
        binding.PlayerIbtn.setOnClickListener(this)
        binding.PlayerIbtn2.setOnClickListener(this)
        binding.PlayerIbtn3.setOnClickListener(this)

        animation = AnimationUtils.loadAnimation(context, R.anim.img_animation)
        animation?.interpolator = LinearInterpolator()
        binding.SeekBar.setOnChangeListener(object : ArcSeekBar.OnChangeListener {
            override fun onStartTrackingTouch(isCanDrag: Boolean) {
                isSeekbarChaning = true
            }

            override fun onProgressChanged(progress: Float, max: Float, fromUser: Boolean) {
                val activity = activity as PlayerActivity?
                activity?.runOnUiThread {
                    if (PlayerActivity.mediaPlayerHelper?.isPlaying == true) {
                        if (isSeekbarChaning) {
                            PlayerActivity.mediaPlayerHelper?.seekTo(binding.SeekBar.getProgress())
                            isSeekbarChaning = false
                        }
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.let { binding.LrcView.updateTime(it.toLong()) }
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                            StaticData.Songs?.getLyric()?.setCurrentPosition(
                                it
                            )
                        }
                        binding.TvStart.setText(
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.toLong()?.div(1000)
                                ?.let { calculateTime(it) }) //开始时间
                        binding.TvEnd.setText(
                            (PlayerActivity.mediaPlayerHelper?.duration?.toLong())?.div(1000)
                                ?.let { calculateTime(it) }) //总时长
                    } else{
                        if (isSeekbarChaning) {
                            PlayerActivity.mediaPlayerHelper?.seekTo(binding.SeekBar.getProgress())
                            isSeekbarChaning = false
                            play()
                        }
                    }
                }
            }

            override fun onStopTrackingTouch(isCanDrag: Boolean) {
                if (PlayerActivity.mediaPlayerHelper?.isPlaying == true) {
                    binding.SeekBar.getProgress()
                    binding.TvStart.setText(
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.toLong()?.div(1000)
                            ?.let { calculateTime(it) })
                }
            }

            override fun onSingleTapUp() {}
        })
        initView()
        val path =
            Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + StaticData.Songs?.getStyle() + "/" + StaticData.Songs?.getId()
        val file1 = File("$path.flac")
        val file2 = File("$path.mp3")
        if ((file1.exists() || file2.exists()) && StaticData.Songs?.getId() !== StaticData.Playing_ID) {
            if (file1.exists()) {
                load("$path.flac", 0)
            } else {
                load("$path.mp3", 0)
            }
        }
        //        else {
//            if(SongData.Songs.getMusic_url() != null && SongData.Songs.getId() != SongData.Playing_ID && CanPlay) {
//                load(SongData.Songs.getMusic_url(),0);
//                CanPlay = false;
//            }
//        }
        binding.playbtn4New.setOnClickListener {
            if(!isReplay) {
                isReplay = true
                binding.playbtn4New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.refresh_pressed
                ) }
                context?.let { it1 -> Toasty.info(it1, "循环播放.", Toast.LENGTH_SHORT, true).show() }
            } else{
                isReplay = false
                binding.playbtn4New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.refresh
                ) }
                context?.let { it1 -> Toasty.info(it1, "取消循环播放.", Toast.LENGTH_SHORT, true).show() }
            }
        }
        binding.playbtn5New.setOnClickListener {
            if(!isDownload) {
                isDownload = true
                binding.playbtn5New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.download_pressed
                ) }
                context?.let { it1 -> Toasty.info(it1, "开始下载歌曲.", Toast.LENGTH_SHORT, true).show() }
            } else{
                isDownload = false
                binding.playbtn5New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.download
                ) }
            }
        }
        binding.playbtn6New.setOnClickListener {
            if(!isAdd) {
                isAdd = true
                binding.playbtn6New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.add_pressed
                ) }
                context?.let { it1 -> Toasty.info(it1, "已添加歌曲.", Toast.LENGTH_SHORT, true).show() }
            } else{
                isAdd = false
                binding.playbtn6New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.add
                ) }
                context?.let { it1 -> Toasty.warning(it1, "已移除歌曲.", Toast.LENGTH_SHORT, true).show() }
            }
        }
        binding.playbtn7New.setOnClickListener {
            if(!isMark) {
                isMark = true
                binding.playbtn7New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.heart_pressed
                ) }
                context?.let { it1 -> Toasty.info(it1, "已收藏歌曲.", Toast.LENGTH_SHORT, true).show() }
            } else{
                isMark = false
                binding.playbtn7New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.heart
                ) }
                context?.let { it1 -> Toasty.warning(it1, "取消收藏!", Toast.LENGTH_SHORT, true).show() }
            }
        }
        binding.playbtn8New.setOnClickListener {
            if(!isComment) {
                isComment = true
                binding.playbtn8New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.chat_pressed
                ) }
                //展开评论区
            } else{
                isComment = false
                binding.playbtn8New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.chat
                ) }
            }
        }
        binding.playbtn9New.setOnClickListener {
            if(!isList) {
                isList = true
                binding.playbtn9New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.list_pressed
                ) }
                //展开播放列表
            } else{
                isList = false
                binding.playbtn9New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                    R.drawable.list
                ) }
            }
        }
        binding.Cover.setOnClickListener {
            val activity = activity as PlayerActivity?
            activity?.goLrc()
        }
    }
    @SuppressLint("ResourceType")
    private fun initView() {
        //歌曲名
        binding.SongName.setText(StaticData.Songs?.getName())
        //歌手名
        var SingerName = ""
        for (i in 0 until (StaticData.Songs?.getSingers()?.size!!)) {
            if (i == 0) {
                SingerName += StaticData.Songs?.getSingers()?.get(i)?.getName()
            } else {
                SingerName += "/" + StaticData.Songs?.getSingers()?.get(i)?.getName()
            }
        }
        binding.SingerName.setText(SingerName)
        if (StaticData.Songs?.getVibrantLight() != null && StaticData.Songs?.getVibrant() != null) {
            binding.SeekBar.setProgressColor(BurnUtil.colorBurn(StaticData.Songs?.getVibrant()!!.getRgb()))
            binding.SeekBar.setNormalColor(StaticData.Songs?.getVibrantLight()!!.getRgb())
        }
        context?.let {
            Glide.with(it)
                .load(StaticData.Songs?.getCover_url())
                .into(binding.Cover)
        }
        if (StaticData.Songs?.getId().equals(StaticData.Playing_ID)) {
            if (PlayerActivity.mediaPlayerHelper?.isPlaying == true) {
                binding.PlayerIbtn.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.playbtn2
                        )
                    }
                )
                PlayerActivity.mediaPlayerHelper?.duration?.let { binding.SeekBar.setMax(it) }
            } else {
                binding.PlayerIbtn.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.playbtn
                        )
                    }
                )
            }
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!isSeekbarChaning) {
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.let { binding.SeekBar.setProgress(it) }
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                            StaticData.Songs?.setCurrentPosition(it)
                        }
                    }
                }
            }, 0, 50)
        } else {
            binding.TvStart.setText(calculateTime(0))
            binding.TvEnd.text =
                StaticData.Songs?.getTime()?.div(1000)?.let { calculateTime(it.toLong()) }
        }
        if (StaticData.Songs?.getLyric() != null) {
            binding.LrcView.loadLrc(StaticData.Songs?.getLyric()?.getContent())
            if (StaticData.Songs?.getVibrantLight() != null && StaticData.Songs?.getVibrant() != null) {
                StaticData.Songs?.getVibrant()?.getRgb()
                    ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
                StaticData.Songs?.getVibrantLight()?.getRgb()
                    ?.let { binding.LrcView.setNormalColor(it) }
                binding.playBtn.setImageTintList(
                    StaticData.Songs?.getVibrantLight()?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
                binding.PlayerIbtn2.setImageTintList(
                    StaticData.Songs?.getVibrantLight()?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
                binding.PlayerIbtn3.setImageTintList(
                    StaticData.Songs?.getVibrantLight()?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
            } else {
                if (StaticData.Songs?.getVibrantDark() != null && StaticData.Songs?.getMuted() != null) {
                    StaticData.Songs?.getMuted()?.getRgb()
                        ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
                    StaticData.Songs?.getVibrantDark()?.getRgb()
                        ?.let { binding.LrcView.setNormalColor(it) }
                    binding.playBtn.setImageTintList(
                        StaticData.Songs?.getVibrantDark()?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                    binding.PlayerIbtn2.setImageTintList(
                        StaticData.Songs?.getVibrantDark()?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                    binding.PlayerIbtn3.setImageTintList(
                        StaticData.Songs?.getVibrantDark()?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                }
            }
        }
        if(isReplay) {
            binding.playbtn4New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.refresh_pressed
            ) }
        } else{
            binding.playbtn4New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.refresh
            ) }
        }
        if(isDownload) {
            binding.playbtn5New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.download_pressed
            ) }
        } else{
            binding.playbtn5New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.download
            ) }
        }
        if(isAdd) {
            binding.playbtn6New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.add_pressed
            ) }
        } else{
            binding.playbtn6New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.add
            ) }
        }
        if(isMark) {
            binding.playbtn7New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.heart_pressed
            ) }
        } else{
            binding.playbtn7New.background = context?.let { it1 -> ContextCompat.getDrawable(it1,
                R.drawable.heart
            ) }
        }
        if (rotate_num > 1) {
            binding.Cover.startAnimation(animation)
        }
        view?.invalidate()
    }
    @SuppressLint("ResourceType")
    private fun initMediaPlayer(url: String, seek: Int) {
        PlayerActivity.mediaPlayerHelper?.setPath(url)
        PlayerActivity.mediaPlayerHelper?.setOnMeidaPlayerHelperListener(object :
            MediaPlayerHelper.OnMeidaPlayerHelperListener {
            override fun onPrepared(mp: MediaPlayer?) {
                PlayerActivity.mediaPlayerHelper?.seekTo(seek) //在当前位置播放
                play()
            }

            override fun onError(mp: MediaPlayer?): Boolean {
                return true
            }

            override fun onCompletion(mp: MediaPlayer?) {
                if (isReplay) {
                    PlayerActivity.mediaPlayerHelper?.start()
                } else {
                    if (PlayerActivity.mediaPlayerHelper?.currentPosition?.plus(1500)!! >= PlayerActivity.mediaPlayerHelper!!.duration!!) {
                        val index3: Int = StaticData.PlayList_now.indexOf(StaticData.Songs)
                        if (index3 < StaticData.PlayList_now.size - 1 && CanPlay) {
                            CanPlay = false
                            val path3 =
                                Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + StaticData.Songs?.getStyle() + "/" + StaticData.PlayList_now.get(
                                    index3 + 1
                                )?.getId()
                            if (File("$path3.flac").exists() || File("$path3.mp3").exists()) {
                                if (File("$path3.flac").exists()) {
                                    StaticData.Songs = StaticData.PlayList_now.get(index3 + 1)
                                    load("$path3.flac", 0)
                                } else {
                                    StaticData.Songs = StaticData.PlayList_now.get(index3 + 1)
                                    load("$path3.mp3", 0)
                                }
                            } else {
//                                if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                                    load(SongData.Songs.getMusic_url(),0);
//                                    CanPlay = false;
//                                } else {
//                                    ExecutorService executorService = Executors.newCachedThreadPool();
//                                    executorService.execute(new GetMusicData(SongData.Songs, SongData.Songs.getId(), "flac"));
//                                    executorService.shutdown();
//                                    try {
//                                        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//                                        if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                                            load(SongData.Songs.getMusic_url(),0);
//                                            CanPlay = false;
//                                        }
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                                context?.let {
                                    Toasty.info(
                                        it,
                                        "开始缓存歌曲.",
                                        Toast.LENGTH_SHORT,
                                        true
                                    ).show()
                                }
                                download(index3 + 1, path3)
                            }
                        }
                    }
                }
            }

            override fun onBufferingUpdate(mp: MediaPlayer?, i: Int) {
//                System.out.println("进度" + i);
//                String path = Environment.getExternalStorageDirectory().getPath() + "/PureMusic/Music/" + SongData.Songs.getStyle() + "/" + SongData.Songs.getId();
//                File file1 = new File(path + ".flac");
//                File file2 = new File(path + ".mp3");
//                if(!isFile) {
//                    if (file1.exists() || file2.exists()) {
//                        isFile = true;
//                        if (file1.exists()) {
//                            load(path + ".flac",PlayerActivity.mediaPlayerHelper.getCurrentPosition());
//                        } else {
//                            load(path + ".mp3",PlayerActivity.mediaPlayerHelper.getCurrentPosition());
//                        }
//                    } else {
//                        int index = SongData.PlayList.indexOf(SongData.Songs);
////                        DU.download(index, path, SongData.PlayList.get(index).getType());
//                        download(index, path, SongData.PlayList.get(index).getType());
//                    }
//                }
            }

        })
        val activity = activity as PlayerActivity?
        activity?.runOnUiThread {
            initView()
        }
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.Player_ibtn -> if (!PlayerActivity.mediaPlayerHelper!!.isPlaying || StaticData.Songs?.getId() !== StaticData.Playing_ID) {
                val path =
                    Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + StaticData.Songs?.getStyle() + "/" + StaticData.Songs?.getId()
                val file1 = File("$path.flac")
                val file2 = File("$path.mp3")
                if (file1.exists() || file2.exists()) {
                    if (StaticData.Songs?.getId() !== StaticData.Playing_ID) {
                        if (file1.exists()) {
                            load("$path.flac", 0)
                        } else {
                            load("$path.mp3", 0)
                        }
                    } else {
                        play()
                    }
                } else {
//                        if(SongData.Songs.getId() != SongData.Playing_ID) {
//                            if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                                load(SongData.Songs.getMusic_url(),0);
//                                CanPlay = false;
//                            } else {
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ExecutorService executorService = Executors.newCachedThreadPool();
//                                        executorService.execute(new GetMusicData(SongData.Songs, SongData.Songs.getId(), "flac"));
//                                        executorService.shutdown();
//                                        try {
//                                            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//                                            if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                                                load(SongData.Songs.getMusic_url(),0);
//                                                CanPlay = false;
//                                            }
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                            }
//                        } else {
//                            play();
//                        }
                    context?.let { Toasty.error(it, "未找到歌曲", Toast.LENGTH_SHORT, true).show() }
                }
            } else {
                PlayerActivity.mediaPlayerHelper?.pause()
                context?.let { Toasty.warning(it, "暂停播放", Toast.LENGTH_SHORT, true).show() }
                binding.PlayerIbtn.setImageDrawable(
                    context?.let {
                        ContextCompat.getDrawable(
                            it,
                            R.drawable.playbtn
                        )
                    }
                )
            }
            R.id.Player_ibtn2 -> {
                val index1: Int = StaticData.PlayList_now.indexOf(StaticData.Songs)
                if (index1 > 0 && CanPlay) {
                    CanPlay = false
                    val path1 =
                        Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + StaticData.Songs?.getStyle() + "/" + StaticData.PlayList_now.get(
                            index1 - 1
                        )?.getId()
                    if (File("$path1.flac").exists() || File("$path1.mp3").exists()) {
                        if (File("$path1.flac").exists()) {
                            StaticData.Songs = StaticData.PlayList_now.get(index1 - 1)
                            load("$path1.flac", 0)
                        } else {
                            StaticData.Songs = StaticData.PlayList_now.get(index1 - 1)
                            load("$path1.mp3", 0)
                        }
                    } else {
//                        if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                            load(SongData.Songs.getMusic_url(),0);
//                            CanPlay = false;
//                        } else {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ExecutorService executorService = Executors.newCachedThreadPool();
//                                    executorService.execute(new GetMusicData(SongData.Songs, SongData.Songs.getId(), "flac"));
//                                    executorService.shutdown();
//                                    try {
//                                        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//                                        if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                                            load(SongData.Songs.getMusic_url(),0);
//                                            CanPlay = false;
//                                        }
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        }
                        context?.let { Toasty.info(it, "开始缓存歌曲.", Toast.LENGTH_SHORT, true).show() }
                        //                        DU.download(index1 - 1,path1,SongData.PlayList.get(index1 - 1).getType());
                        download(index1 - 1, path1)
                    }
                }
            }
            R.id.Player_ibtn3 -> {
                val index2: Int = StaticData.PlayList_now.indexOf(StaticData.Songs)
                if (index2 < StaticData.PlayList_now.size - 1 && CanPlay) {
                    CanPlay = false
                    val path2 =
                        Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + StaticData.Songs?.getStyle() + "/" + StaticData.PlayList_now.get(
                            index2 + 1
                        )?.getId()
                    if (File("$path2.flac").exists() || File("$path2.mp3").exists()) {
                        if (File("$path2.flac").exists()) {
                            StaticData.Songs = StaticData.PlayList_now.get(index2 + 1)
                            load("$path2.flac", 0)
                        } else {
                            StaticData.Songs = StaticData.PlayList_now.get(index2 + 1)
                            load("$path2.mp3", 0)
                        }
                    } else {
//                        if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                            load(SongData.Songs.getMusic_url(),0);
//                            CanPlay = false;
//                        } else {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ExecutorService executorService = Executors.newCachedThreadPool();
//                                    executorService.execute(new GetMusicData(SongData.Songs, SongData.Songs.getId(), "flac"));
//                                    executorService.shutdown();
//                                    try {
//                                        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//                                        if(SongData.Songs.getMusic_url() != null && CanPlay) {
//                                            load(SongData.Songs.getMusic_url(),0);
//                                            CanPlay = false;
//                                        }
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        }
//                        DU.download(index2 + 1,path2,SongData.PlayList.get(index2 + 1).getType());
                        context?.let { Toasty.info(it, "开始缓存歌曲.", Toast.LENGTH_SHORT, true).show() }
                        download(index2 + 1, path2)
                    }
                }
            }
            else -> {}
        }
    }

    fun play() {
        CanPlay = true
        StaticData.Playing_ID = StaticData.Songs?.getId().toString()
        if (StaticData.isFirstPlay) {
            StaticData.PlayList_now = StaticData.PlayList
            StaticData.isFirstPlay = false
        }
        if (!PlayerActivity.mediaPlayerHelper?.isPlaying!!) {
            context?.let {
                Toasty.custom(
                    it,
                    "开始播放  " + "\"" + StaticData.Songs?.getName() + "\"",
                    R.mipmap.ic_launcher_round,
                    R.color.material_blue_800,
                    750,
                    false,
                    true
                ).show()
            }
        }
        rotate_num += 1
        if (rotate_num == 1) {
            binding.Cover.startAnimation(animation)
        }
        PlayerActivity.mediaPlayerHelper?.start()
        binding.PlayerIbtn.setImageDrawable(
            context?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.playbtn2
                )
            }
        )
        PlayerActivity.mediaPlayerHelper?.duration?.let { binding.SeekBar.setMax(it) } //将音乐总时间设置为Seekbar的最大值
        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (!isSeekbarChaning) {
                    val activity = activity as PlayerActivity?
                    activity?.runOnUiThread {
                        if (!isSeekbarChaning) {
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.let { binding.LrcView.updateTime(it.toLong()) }
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                                StaticData.Songs?.getLyric()?.setCurrentPosition(
                                    it
                                )
                            }
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.let { binding.SeekBar.setProgress(it) }
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                                StaticData.Songs?.setCurrentPosition(it)
                            }
                        }
                    }
                }
            }
        }, 0, 50)
    }

    fun calculateTime(time: Long): String {
        val minute: Int
        val second: Int
        return if (time >= 60) {
            minute = (time / 60).toInt()
            second = (time % 60).toInt()
            //分钟在0~9
            if (minute < 10) {
                //判断秒
                if (second < 10) {
                    "0$minute:0$second"
                } else {
                    "0$minute:$second"
                }
            } else {
                //分钟大于10再判断秒
                if (second < 10) {
                    "$minute:0$second"
                } else {
                    "$minute:$second"
                }
            }
        } else {
            second = time.toInt()
            if (second >= 0 && second < 10) {
                "00:0$second"
            } else {
                "00:$second"
            }
        }
    }

    private fun load(url: String, Position: Int) {
        Thread {
            if(!StaticData.Songs?.isCould()!!) {
                if (StaticData.Songs?.getLyric() == null) {
                    val pools = Executors.newCachedThreadPool()
                    pools.submit(GetLyricData(StaticData.Songs, StaticData.Songs?.getId()))
                    pools.submit() {
                        val p = Palette.from(getBitmapGlide(StaticData.Songs?.getCover_url())!!)
                            .generate()
                        StaticData.Songs?.setVibrant(p.vibrantSwatch)
                        StaticData.Songs?.setVibrantLight(p.lightVibrantSwatch)
                        StaticData.Songs?.setVibrantDark(p.darkVibrantSwatch)
                        StaticData.Songs?.setMuted(p.mutedSwatch)
                    }
                    pools.shutdown()
                    pools.awaitTermination(
                        Long.MAX_VALUE,
                        TimeUnit.NANOSECONDS
                    )
                    CanPlay = true
                    initMediaPlayer(url, Position)
                } else {
                    CanPlay = true
                    initMediaPlayer(url, Position)
                }
            }
        }.start()
    }

    fun getBitmapGlide(url: String?): Bitmap? {
        try {
            return Glide.with(this@PlayerFragment)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    fun download(index: Int, path: String) {
        Thread {
            val pools = Executors.newCachedThreadPool()
            if(!StaticData.Songs?.isCould()!!){
                pools.submit(StaticData.PlayList_now.get(index)?.getId()?.let {
                    GetMusicData(
                        StaticData.PlayList_now.get(index), it
                    )
                }
                )
            }
            pools.shutdown()
            pools.awaitTermination(
                Long.MAX_VALUE,
                TimeUnit.NANOSECONDS
            )
            FileDownloader.getImpl().create(StaticData.PlayList_now.get(index)?.getMusic_url())
                .setPath(path + "." + StaticData.PlayList_now.get(index)?.getType())
                .setListener(object : FileDownloadListener() {
                    override fun pending(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                    }

                    override fun progress(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                    }

                    override fun completed(task: BaseDownloadTask) {
                        context?.let {
                            Toasty.success(it, "缓存成功.", Toast.LENGTH_SHORT, true)
                                .show()
                        }
                        StaticData.Songs = StaticData.PlayList_now.get(index)
                        load(path + "." + StaticData.PlayList_now.get(index)?.getType(), 0)
                    }

                    override fun paused(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                    }

                    override fun error(task: BaseDownloadTask, e: Throwable) {
                        context?.let {
                            Toasty.error(
                                it,
                                "网络问题,请稍后再试!",
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        }
                    }

                    override fun warn(task: BaseDownloadTask) {}
                })
                .start()
        }.start()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PlayerFragment()
        var isReplay = false
        var isDownload = false
        var isAdd = false
        var isMark = true
        var isComment = false
        var isList = false
        var isRotate = false
        var rotate_num = 0
    }
}