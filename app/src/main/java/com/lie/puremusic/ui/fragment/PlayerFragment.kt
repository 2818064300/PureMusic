package com.lie.puremusic.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.king.view.arcseekbar.ArcSeekBar
import com.lie.puremusic.*
import com.lie.puremusic.databinding.FragmentPlayerBinding
import com.lie.puremusic.music.netease.data.SongUrlData
import com.lie.puremusic.music.netease.data.toStandard
import com.lie.puremusic.music.netease.data.toStandardSongDataArrayList
import com.lie.puremusic.service.ServiceSongUrl
import com.lie.puremusic.standard.data.StandardSongDataEx
import com.lie.puremusic.ui.activity.MainActivity
import com.lie.puremusic.ui.activity.PlayerActivity
import com.lie.puremusic.ui.dialog.PlaylistDialog
import com.lie.puremusic.utils.*
import com.lie.puremusic.utils.MagicHttp.runOnMainThread
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import java.io.File
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

class PlayerFragment : Fragment(), View.OnClickListener {
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
        binding.MyWave.waveHeight = 180
        binding.MyWave.setStrokeWidth(6f)
        binding.MyWave.setWaveColor("#72bcce")
        binding.MyWave.waveSpeed = 40
        binding.MyWave2.waveHeight = 320
        binding.MyWave2.setStrokeWidth(6f)
        binding.MyWave2.setWaveColor("#eff9fa")
        binding.MyWave2.waveSpeed = 40
        binding.MyWave2.setReverse(true)
        binding.MyWave3.waveHeight = 12
        binding.MyWave3.setWaveColor("#f1f5f6")
        binding.MyWave3.waveSpeed = 40

        binding.LrcView.setLabel("聆听好音乐")
        binding.LrcView.setCurrentColor(Color.parseColor("#6399fd"))
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
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                            binding.LrcView.updateTime(
                                it.toLong()
                            )
                        }
                        StaticData.CurrentPosition =
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.toLong()
                        binding.TvStart.setText(
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.toLong()?.div(1000)
                                ?.let { calculateTime(it) }) //开始时间
                        binding.TvEnd.setText(
                            (PlayerActivity.mediaPlayerHelper?.duration?.toLong())?.div(1000)
                                ?.let { calculateTime(it) }) //总时长
                    } else {
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
        var type: String = if (StaticData.isCloud) {
            "Cloud"
        } else {
            "Netease"
        }
        val path =
            Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + type + "/" + StaticData.Songs?.id
        val file1 = File("$path.flac")
        val file2 = File("$path.mp3")
        if ((file1.exists() || file2.exists()) && StaticData.Songs?.id !== StaticData.Playing_ID) {
            if (file1.exists()) {
                load("$path.flac", "flac", 0)
            } else {
                load("$path.mp3", "mp3", 0)
            }
        }
        binding.playbtn4New.setOnClickListener {
            if (!isReplay) {
                isReplay = true
                binding.playbtn4New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.refresh_pressed)
                Toasty.info(requireContext(), "循环播放.", Toast.LENGTH_SHORT, true).show()
            } else {
                isReplay = false
                binding.playbtn4New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.refresh)
                Toasty.info(requireContext(), "取消循环播放.", Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.playbtn5New.setOnClickListener {
            if (!isDownload) {
                isDownload = true
                binding.playbtn5New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.download_pressed)
                Toasty.info(requireContext(), "开始下载歌曲.", Toast.LENGTH_SHORT, true).show()
            } else {
                isDownload = false
                binding.playbtn5New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.download)
            }
        }
        binding.playbtn6New.setOnClickListener {
            if (!isAdd) {
                isAdd = true
                binding.playbtn6New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.add_pressed)
                Toasty.info(requireContext(), "已添加歌曲.", Toast.LENGTH_SHORT, true).show()
            } else {
                isAdd = false
                binding.playbtn6New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.add)
                Toasty.warning(requireContext(), "已移除歌曲.", Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.playbtn7New.setOnClickListener {
            if (!isMark) {
                isMark = true
                binding.playbtn7New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.heart_pressed)
                Toasty.info(requireContext(), "已收藏歌曲.", Toast.LENGTH_SHORT, true).show()
            } else {
                isMark = false
                binding.playbtn7New.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.heart)
                Toasty.warning(requireContext(), "取消收藏!", Toast.LENGTH_SHORT, true).show()
            }
        }
        binding.ivComment.setOnClickListener {
            if (!StaticData.isCloud) {
                startActivity(
                    Intent(
                        activity,
                        com.lie.puremusic.ui.activity.CommentActivity::class.java
                    )
                )
                activity?.overridePendingTransition(
                    R.anim.anim_slide_enter_bottom,
                    R.anim.anim_no_anim
                )
            }
        }
        binding.playbtn9New.setOnClickListener {
            singleClick {
                PlaylistDialog().show(PlayerActivity.fragmentManager, null)
            }
        }
        binding.Cover.setOnClickListener {
            val activity = activity as PlayerActivity?
            activity?.goLrc()
        }
        binding.Cover.setOnLongClickListener {
            //震动
            ContextCompat.getSystemService(requireContext(), Vibrator::class.java)?.vibrate(100)
            if (!File(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/Song/" + StaticData.Songs?.id + ".png").exists()) {
                File(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/Song/")
                    .mkdirs()
                FileDownloader.getImpl().create(StaticData.Songs?.imageUrl)
                    .setPath(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/Song/" + "${StaticData.Songs?.id}.png")
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
                            Toasty.success(
                                MainActivity.context,
                                "封面保存成功",
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        }

                        override fun paused(
                            task: BaseDownloadTask,
                            soFarBytes: Int,
                            totalBytes: Int
                        ) {
                        }

                        override fun error(task: BaseDownloadTask, e: Throwable) {
                            Toasty.error(
                                MainActivity.context,
                                "网络问题,请稍后再试!",
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        }

                        override fun warn(task: BaseDownloadTask) {}
                    })
                    .start()
            } else {
                Toasty.info(
                    MainActivity.context,
                    "已为您保存过了哦QWQ.",
                    Toast.LENGTH_SHORT,
                    true
                ).show()
            }
            true
        }
    }

    @SuppressLint("ResourceType")
    private fun initView() {
        //歌曲名
        binding.SongName.setText(StaticData.Songs?.name)
        //歌手名
        binding.SingerName.text = StaticData.Songs?.artists?.parse()
        if (StaticData.PlayDataEx?.VibrantLight != null && StaticData.PlayDataEx?.Vibrant != null) {
            binding.SeekBar.setProgressColor(BurnUtil.colorBurn(StaticData.PlayDataEx?.Vibrant!!.getRgb()))
            binding.SeekBar.setNormalColor(StaticData.PlayDataEx?.VibrantLight!!.getRgb())
        }
        context?.let {
            Glide.with(it)
                .load(StaticData.Songs?.imageUrl)
                .into(binding.Cover)
        }
        if (StaticData.Songs?.id.equals(StaticData.Playing_ID)) {
            if (PlayerActivity.mediaPlayerHelper?.isPlaying == true) {
                binding.PlayerIbtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.playbtn2
                    )
                )
                PlayerActivity.mediaPlayerHelper?.duration?.let { binding.SeekBar.setMax(it) }
            } else {
                binding.PlayerIbtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.playbtn
                    )
                )
            }
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!isSeekbarChaning) {
                        PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                            binding.SeekBar.setProgress(
                                it
                            )
                        }
                        StaticData.CurrentPosition =
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.toLong()
                    }
                }
            }, 0, 50)
        } else {
            binding.TvStart.setText(calculateTime(0))
//            binding.TvEnd.text = StaticData.Songs?.getTime()?.div(1000)?.let { calculateTime(it.toLong()) }
            binding.TvEnd.text = calculateTime(0)
        }
        if (StaticData.SongUrl != null) {
            if(!StaticData.isCloud){
                StaticData.Songs?.let {
                    ServiceSongUrl.getLyric(it) {
                        StaticData.SongLrc = it
                        binding.LrcView.loadLyric(it.lyric, it.secondLyric)
                    }
                }
            }
            if (StaticData.PlayDataEx?.VibrantLight != null && StaticData.PlayDataEx?.Vibrant != null) {
                StaticData.PlayDataEx?.Vibrant?.getRgb()
                    ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
                StaticData.PlayDataEx?.VibrantLight?.getRgb()
                    ?.let { binding.LrcView.setNormalColor(it) }
                StaticData.PlayDataEx?.Vibrant?.getRgb()
                    ?.let { binding.MyWave.setWaveColor(it) }
                StaticData.PlayDataEx?.VibrantLight?.getRgb()
                    ?.let { binding.MyWave2.setWaveColor(it) }
                binding.seekbar3Item1.setImageTintList(
                    StaticData.PlayDataEx?.Vibrant?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
                binding.seekbar3Item2.setImageTintList(
                    StaticData.PlayDataEx?.VibrantLight?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
                binding.playBtn.setImageTintList(
                    StaticData.PlayDataEx?.VibrantLight?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
                binding.PlayerIbtn2.setImageTintList(
                    StaticData.PlayDataEx?.VibrantLight?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
                binding.PlayerIbtn3.setImageTintList(
                    StaticData.PlayDataEx?.VibrantLight?.getRgb()?.let {
                        ColorStateList.valueOf(
                            it
                        )
                    }
                )
            } else {
                if (StaticData.PlayDataEx?.VibrantDark != null && StaticData.PlayDataEx?.Muted != null) {
                    StaticData.PlayDataEx?.Muted?.getRgb()
                        ?.let { BurnUtil.colorBurn(it) }
                        ?.let { binding.LrcView.setCurrentColor(it) }
                    StaticData.PlayDataEx?.VibrantDark?.getRgb()
                        ?.let { binding.LrcView.setNormalColor(it) }
                    StaticData.PlayDataEx?.Vibrant?.getRgb()
                        ?.let { binding.MyWave.setWaveColor(it) }
                    StaticData.PlayDataEx?.VibrantLight?.getRgb()
                        ?.let { binding.MyWave2.setWaveColor(it) }
                    binding.seekbar3Item1.setImageTintList(
                        StaticData.PlayDataEx?.Muted?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                    binding.seekbar3Item2.setImageTintList(
                        StaticData.PlayDataEx?.VibrantDark?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                    binding.playBtn.setImageTintList(
                        StaticData.PlayDataEx?.VibrantDark?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                    binding.PlayerIbtn2.setImageTintList(
                        StaticData.PlayDataEx?.VibrantDark?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                    binding.PlayerIbtn3.setImageTintList(
                        StaticData.PlayDataEx?.VibrantDark?.getRgb()?.let {
                            ColorStateList.valueOf(
                                it
                            )
                        }
                    )
                }
            }
        }
        if (isReplay) {
            binding.playbtn4New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.refresh_pressed)
        } else {
            binding.playbtn4New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.refresh)
        }
        if (isDownload) {
            binding.playbtn5New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.download_pressed)
        } else {
            binding.playbtn5New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.download)
        }
        if (isAdd) {
            binding.playbtn6New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.add_pressed)
        } else {
            binding.playbtn6New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.add)
        }
        if (isMark) {
            binding.playbtn7New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.heart_pressed)
        } else {
            binding.playbtn7New.background =
                ContextCompat.getDrawable(MainActivity.context, R.drawable.heart)
        }

        if (rotate_num >= 1) {
            binding.Cover.startAnimation(animation)
        }
        if (StaticData.Style.equals("SongList")) {
            StaticData.isCloud = false
            StaticData.PlayList_now = StaticData.PlayListData?.songs
        }
        if (StaticData.Style.equals("SingerList")) {
            StaticData.isCloud = false
            StaticData.PlayList_now = StaticData.SingerData?.songs
        }
        if (StaticData.Style.equals("Search")) {
            StaticData.isCloud = false
            StaticData.PlayList_now = StaticData.SearchResult
        }
        if (StaticData.Style.equals("Cloud")) {
            StaticData.isCloud = true
            StaticData.PlayList_now = StaticData.Cloud
        }
        if (StaticData.Style.equals("TopList")) {
            StaticData.isCloud = false
            StaticData.PlayList_now = StaticData.NewSong
        }
        if (StaticData.Style.equals("DailyRecommend")) {
            StaticData.isCloud = false
            StaticData.PlayList_now =
                StaticData.DailyRecommendSongData?.data?.dailySongs?.toStandardSongDataArrayList()
        }
        if (StaticData.Style.equals("UserCloud")) {
            StaticData.isCloud = false
            StaticData.PlayList_now = StaticData.UserCloudData?.data?.toStandard()
        }
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
                        if (StaticData.Position < StaticData.PlayList_now!!.size - 1 && CanPlay) {
                            CanPlay = false
                            var type: String = if (StaticData.isCloud) {
                                "Cloud"
                            } else {
                                "Netease"
                            }
                            val path3 =
                                Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + type + "/" + StaticData.PlayList_now?.get(
                                    StaticData.Position + 1
                                )?.id
                            if (File("$path3.flac").exists() || File("$path3.mp3").exists()) {
                                StaticData.Position += 1
                                if (File("$path3.flac").exists()) {
                                    StaticData.Songs =
                                        StaticData.PlayList_now?.get(StaticData.Position)
                                    load("$path3.flac", "flac", 0)
                                } else {
                                    StaticData.Songs =
                                        StaticData.PlayList_now?.get(StaticData.Position)
                                    load("$path3.mp3", "mp3", 0)
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
                                download(StaticData.Position + 1, path3)
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
        MagicHttp.runOnMainThread {
            initView()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.Player_ibtn -> if (!PlayerActivity.mediaPlayerHelper!!.isPlaying || StaticData.Songs?.id !== StaticData.Playing_ID) {
                var type: String = if (StaticData.isCloud) {
                    "Cloud"
                } else {
                    "Netease"
                }
                val path =
                    Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + type + "/" + StaticData.Songs?.id
                val file1 = File("$path.flac")
                val file2 = File("$path.mp3")
                if (file1.exists() || file2.exists()) {
                    if (StaticData.Songs?.id !== StaticData.Playing_ID) {
                        if (file1.exists()) {
                            load("$path.flac", "flac", 0)
                        } else {
                            load("$path.mp3", "mp3", 0)
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
                    Toasty.error(requireContext(), "未找到歌曲.", Toast.LENGTH_SHORT, true).show()
                }
            } else {
                PlayerActivity.mediaPlayerHelper?.pause()
                Toasty.warning(requireContext(), "暂停播放.", Toast.LENGTH_SHORT, true).show()
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
                if (StaticData.Position > 0 && CanPlay) {
                    CanPlay = false
                    var type: String = if (StaticData.isCloud) {
                        "Cloud"
                    } else {
                        "Netease"
                    }
                    val path1 =
                        Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + type + "/" + StaticData.PlayList_now?.get(
                            StaticData.Position - 1
                        )?.id
                    if (File("$path1.flac").exists() || File("$path1.mp3").exists()) {
                        StaticData.Position -= 1
                        if (File("$path1.flac").exists()) {
                            StaticData.Songs =
                                StaticData.PlayList_now?.get(StaticData.Position)
                            load("$path1.flac", "flac", 0)
                        } else {
                            StaticData.Songs =
                                StaticData.PlayList_now?.get(StaticData.Position)
                            load("$path1.mp3", "mp3", 0)
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
                        download(StaticData.Position - 1, path1)
                    }
                }
            }
            R.id.Player_ibtn3 -> {
                if (StaticData.Position < StaticData.PlayList_now!!.size - 1 && CanPlay) {
                    CanPlay = false
                    var type: String = if (StaticData.isCloud) {
                        "Cloud"
                    } else {
                        "Netease"
                    }
                    val path2 =
                        Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + type + "/" + StaticData.PlayList_now?.get(
                            StaticData.Position + 1
                        )?.id
                    if (File("$path2.flac").exists() || File("$path2.mp3").exists()) {
                        StaticData.Position += 1
                        if (File("$path2.flac").exists()) {
                            StaticData.Songs =
                                StaticData.PlayList_now?.get(StaticData.Position)
                            load("$path2.flac", "flac", 0)
                        } else {
                            StaticData.Songs =
                                StaticData.PlayList_now?.get(StaticData.Position)
                            load("$path2.mp3", "mp3", 0)
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
                        download(StaticData.Position + 1, path2)
                    }
                }
            }
            else -> {}
        }
    }

    fun play() {
        CanPlay = true
        StaticData.Playing_ID = StaticData.Songs?.id.toString()
        if (StaticData.isFirstPlay) {
            if (StaticData.Style.equals("SongList")) {
                StaticData.isCloud = false
                StaticData.PlayList_now = StaticData.PlayListData?.songs
            }
            if (StaticData.Style.equals("SingerList")) {
                StaticData.isCloud = false
                StaticData.PlayList_now = StaticData.SingerData?.songs
            }
            if (StaticData.Style.equals("Search")) {
                StaticData.isCloud = false
                StaticData.PlayList_now = StaticData.SearchResult
            }
            if (StaticData.Style.equals("Cloud")) {
                StaticData.isCloud = true
                StaticData.PlayList_now = StaticData.Cloud
            }
            if (StaticData.Style.equals("TopList")) {
                StaticData.isCloud = false
                StaticData.PlayList_now = StaticData.NewSong
            }
            if (StaticData.Style.equals("DailyRecommend")) {
                StaticData.isCloud = false
                StaticData.PlayList_now =
                    StaticData.DailyRecommendSongData?.data?.dailySongs?.toStandardSongDataArrayList()
            }
            if (StaticData.Style.equals("UserCloud")) {
                StaticData.isCloud = false
                StaticData.PlayList_now = StaticData.UserCloudData?.data?.toStandard()
            }
            StaticData.isFirstPlay = false
        }
        if (!PlayerActivity.mediaPlayerHelper?.isPlaying!!) {
            Toasty.custom(
                MainActivity.context,
                "开始播放  " + "\"" + StaticData.Songs?.name + "\"",
                R.mipmap.ic_launcher_round,
                R.color.material_blue_800,
                750,
                false,
                true
            ).show()
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
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                                binding.LrcView.updateTime(
                                    it.toLong()
                                )
                            }
                            PlayerActivity.mediaPlayerHelper?.currentPosition?.let {
                                binding.SeekBar.setProgress(
                                    it
                                )
                            }
                            StaticData.CurrentPosition =
                                PlayerActivity.mediaPlayerHelper?.currentPosition?.toLong()
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

    private fun load(url: String, type: String, Position: Int) {
        runOnMainThread {
            if (!StaticData.isCloud) {
                if (StaticData.SongUrl == null) {
                    val pools = Executors.newCachedThreadPool()
                    var Vibrant: Palette.Swatch? = null
                    var VibrantLight: Palette.Swatch? = null
                    var VibrantDark: Palette.Swatch? = null
                    var Muted: Palette.Swatch? = null
                    pools.execute {
                        StaticData.Songs?.let {
                            ServiceSongUrl.getLyric(it) {
                                StaticData.SongLrc = it
                            }
                        }
                    }
                    val p: Palette? =
                        getBitmapGlide(StaticData.Songs?.imageUrl)?.let { it1 ->
                            Palette.from(it1)
                                .generate()
                        }
                    Vibrant = p?.getVibrantSwatch()
                    VibrantLight = p?.getLightVibrantSwatch()
                    VibrantDark = p?.getDarkVibrantSwatch()
                    Muted = p?.getMutedSwatch()
                    StaticData.PlayDataEx = StandardSongDataEx(
                        StaticData.Songs?.id,
                        Vibrant,
                        VibrantLight,
                        VibrantDark,
                        Muted
                    )
                    StaticData.SongUrl = if (type.equals("flac")){
                        SongUrlData.UrlData(-1, url, -1, -1, type,"lossless")
                    } else{
                        SongUrlData.UrlData(-1, url, -1, -1, type,"standard")
                    }
                    initMediaPlayer(url, Position)
                } else {
                    CanPlay = true
                    initMediaPlayer(url, Position)
                }
            } else {
                CanPlay = true
                StaticData.SongUrl = SongUrlData.UrlData(
                    -1,
                    "http://puremusic.com.cn/Cloud/Music/music" + (StaticData.Position + 1) + ".mp3",
                    -1,
                    -1,
                    "mp3",
                    "standard"
                )
                initMediaPlayer(url, Position)
            }
        }
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
        Toasty.info(
            MainActivity.context,
            "开始缓存歌曲.",
            Toast.LENGTH_SHORT,
            true
        ).show()
        Thread {
            val pools = Executors.newCachedThreadPool()
            pools.execute {
                ServiceSongUrl.getUrl(StaticData.PlayList_now?.get(index)) {
                    if (StaticData.isCloud) {
                        StaticData.SongUrl = SongUrlData.UrlData(
                            -1,
                            "http://puremusic.com.cn/Cloud/Music/music" + (index + 1) + ".mp3",
                            -1,
                            -1,
                            "mp3",
                            "standard"
                        )
                    } else {
                        StaticData.SongUrl = it
                    }
                    FileDownloader.getImpl().create(StaticData.SongUrl?.url)
                        .setPath(path + "." + StaticData.SongUrl?.type)
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
                                Toasty.success(
                                    MainActivity.context,
                                    "缓存成功.",
                                    Toast.LENGTH_SHORT,
                                    true
                                )
                                    .show()
                                StaticData.Position = index
                                StaticData.Songs = StaticData.PlayList_now?.get(index)
                                load(
                                    path + "." + StaticData.SongUrl?.type,
                                    StaticData.SongUrl?.type ?: "flac",
                                    0
                                )
                            }

                            override fun paused(
                                task: BaseDownloadTask,
                                soFarBytes: Int,
                                totalBytes: Int
                            ) {
                            }

                            override fun error(task: BaseDownloadTask, e: Throwable) {
                                Toasty.error(
                                    MainActivity.context,
                                    "网络问题,请稍后再试!",
                                    Toast.LENGTH_SHORT,
                                    true
                                ).show()
                            }

                            override fun warn(task: BaseDownloadTask) {}
                        })
                        .start()

                }
            }
            pools.shutdown()
        }.start()
    }

    override fun onResume() {
        super.onResume()
        if (rotate_num >= 1) {
            binding.Cover.startAnimation(animation)
        }
    }

    companion object {
        @JvmStatic
        var isReplay = false
        var isDownload = false
        var isAdd = false
        var isMark = true
        var rotate_num = 0
    }
}