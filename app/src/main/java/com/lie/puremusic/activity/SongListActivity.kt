package com.lie.puremusic.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ktx.immersionBar
import com.lie.puremusic.*
import com.lie.puremusic.adapter.MediaPlayerHelper
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivitySongListBinding
import com.lie.puremusic.listener.AppBarStateChangeListener
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.pojo.SongList
import com.lie.puremusic.utils.CropTransformation
import com.lie.puremusic.utils.GetSongData
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors

class SongListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySongListBinding
    private var SongList: SongList? = null
    private val btn_group: MutableList<ImageButton?> = ArrayList()
    private val tv_group: MutableList<TextView?> = ArrayList()
    private var last_SelectID = 0
    private var animation: Animation? = null
    private val pools = Executors.newCachedThreadPool()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        immersionBar {
            statusBarColor(R.color.nullcolor)
            statusBarDarkFont(true)
        }
        initbtn()
        binding.PlayBar.visibility = View.GONE
        binding.Avatar2.visibility = View.GONE
        animation = AnimationUtils.loadAnimation(this, R.anim.img_animation)
        animation?.interpolator = LinearInterpolator()
        SongList = StaticData.Home.getSongsLists()?.get(intent.getIntExtra("index", Int.MAX_VALUE))


        when (intent.getStringExtra("style")) {
            "SingerList" -> SongList = StaticData.Home.getSongsLists()?.get(intent.getIntExtra("index", Int.MAX_VALUE))
            "SearchList" -> SongList = StaticData.Result.getSongLists()?.get(intent.getIntExtra("index", Int.MAX_VALUE))
            "List_ID" -> SongList = StaticData.SongList
            "UserList" -> SongList = StaticData.User?.getSongLists()?.get(intent.getIntExtra("index", Int.MAX_VALUE))
            "SquareList" -> StaticData.Square.get(StaticData.Square_SelectID)?.getSongsLists()?.get(intent.getIntExtra("index", Int.MAX_VALUE))
        }

        if (StaticData.Containr.contains(SongList?.getId())) {
            binding.RefreshLayout.autoRefresh(200)
            overridePendingTransition(R.anim.top_in, R.anim.top_out)
        } else {
            StaticData.Containr.add(this.SongList?.getId())
            binding.RefreshLayout.autoRefresh(500)
        }

        binding.SonglistName.text = SongList?.getName()
        binding.SonglistCount.text = "共 " + SongList?.getCount() + " 首"
        binding.tips1.text = SongList?.getName()
        binding.tips2.text = "共 " + SongList?.getCount() + " 首"
        binding.Back.setOnClickListener { finish() }
        binding.FAB.setOnClickListener { binding.RefreshLayout.autoRefresh() }
        val typeface = Typeface.createFromAsset(this.assets, "汉仪雅酷黑65W.ttf")
        binding.Title.setTypeface(typeface)
        binding.FAB.visibility = View.GONE
        binding.FAB.setOnLongClickListener {
            binding.SonglistRv.smoothScrollToPosition(0)
            false
        }
        binding.CenterAppbarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state === State.EXPANDED) {
                    binding.Container1.visibility = View.VISIBLE
                    binding.Container2.visibility = View.VISIBLE
                    binding.Container3.visibility = View.GONE
                    binding.FAB.visibility = View.GONE
                } else if (state === State.COLLAPSED) {
                    binding.Container1.visibility = View.GONE
                    binding.Container2.visibility = View.GONE
                    binding.Container3.visibility = View.VISIBLE
                    if (StaticData.Songs == null) {
                        binding.FAB.visibility = View.VISIBLE
                    }
                } else {
                    binding.Container1.visibility = View.VISIBLE
                    binding.Container2.visibility = View.VISIBLE
                    binding.Container3.visibility = View.GONE
                    binding.FAB.visibility = View.GONE
                }
            }
        })
        binding.PlayBar.setOnClickListener {
            this@SongListActivity.startActivity(
                Intent(
                    this@SongListActivity,
                    PlayerActivity::class.java
                )
            )
        }
        binding.PlayButton.setOnClickListener {
            if (MediaPlayerHelper.getInstance(this@SongListActivity)?.isPlaying == true) {
                MediaPlayerHelper.getInstance(this@SongListActivity)?.pause()
                binding.PlayButton.setBackgroundResource(R.drawable.play)
            } else {
                MediaPlayerHelper.getInstance(this@SongListActivity)?.start()
                binding.PlayButton.setBackgroundResource(R.drawable.play_pressed)
            }
        }
        Glide.with(this@SongListActivity)
            .load(SongList?.getCover_url() + "?param=1440y1440")
            .into(binding.Avatar)
        Glide.with(this@SongListActivity)
            .load(SongList?.getCover_url() + "?param=1440y1440")
            .transform(
                CropTransformation(
                    0,
                    0,
                    (1260 * StaticData.Scale).toInt(),
                    (910 * StaticData.Scale).toInt()
                )
            )
            .into(binding.SonglistBg)
        Glide.with(this@SongListActivity)
            .load(SongList?.getCover_url() + "?param=1440y1440")
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation<Bitmap?>(
                        CropTransformation(
                            0,
                            (910 * StaticData.Scale).toInt(),
                            (1260 * StaticData.Scale).toInt(),
                            (280 * StaticData.Scale).toInt()
                        ), BlurTransformation(25, 8), FitCenter()
                    )
                )
            )
            .into(binding.SonglistBg2)
        //        Glide.with(SongListActivity.this)
//                .load(SongList.getCover_url() + "?param=1440y1440")
//                .transform(new CropTransformation(0,0,944,683))
//                .into(binding.SonglistBg);
//        Glide.with(SongListActivity.this)
//                .load(SongList.getCover_url() + "?param=1440y1440")
//                .apply(bitmapTransform(new MultiTransformation(new CropTransformation(0,683,944,210),new BlurTransformation(25, 8),new FitCenter())))
//                .into(binding.SonglistBg2);
        FileDownloader.setup(this)
        binding.SonglistBg.setOnLongClickListener {
            File(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/SongList/")
                .mkdirs()
            FileDownloader.getImpl().create(SongList?.getCover_url())
                .setPath(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/SongList/" + SongList?.getId() + ".png")
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
                            this@SongListActivity,
                            "缓存成功.",
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
                            this@SongListActivity,
                            "网络问题,请稍后再试!",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }

                    override fun warn(task: BaseDownloadTask) {}
                })
                .start()
            false
        }
        Thread {
            for (j in 0 until StaticData.PlayList.size) {
                pools.submit {
                    var url =
                        "http://www.puremusic.com.cn:3000/song/detail?ids=" + StaticData.PlayList
                            ?.get(j)?.getId()
                    val request: Request = Request.Builder()
                        .url(url)
                        .addHeader("cookie", StaticData.cookie)
                        .method("GET", null)
                        .build()

                    OkHttpClient().newCall(request)
                        .enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                            }

                            override fun onResponse(call: Call, response: Response) {
                                val songs = JSONObject(response.body?.string()).getJSONArray("songs")
                                var Songs = StaticData.PlayList.get(j)
                                Songs?.setName(songs.getJSONObject(0).getString("name"))
                                for (i in 0 until songs.getJSONObject(0).getJSONArray("ar")
                                    .length()) {
                                    val singer = Singer(
                                        songs.getJSONObject(0).getJSONArray("ar").getJSONObject(i)
                                            .getString("id")
                                    )
                                    singer.setName(
                                        songs.getJSONObject(0).getJSONArray("ar").getJSONObject(i)
                                            .getString("name")
                                    )
                                    Songs?.getSingers()?.add(singer)
                                }
                                Songs?.setCover_url(
                                    songs.getJSONObject(0).getJSONObject("al").getString("picUrl")
                                )
                                Songs?.setFee(songs.getJSONObject(0).getInt("fee"))
                                Songs?.setPop(songs.getJSONObject(0).getInt("pop"))
                                Songs?.setTime(songs.getJSONObject(0).getInt("dt"))
                                Songs?.setStyle("NeteaseCloudMusic")
                            }
                        })

                }
            }
            pools.shutdown()
        }.start()
        val adapter = MyRecyclerAdapter(
            this,
            StaticData.PlayList
        )
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.SonglistRv.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.SonglistRv.layoutManager =
            LinearLayoutManager(this@SongListActivity, LinearLayoutManager.VERTICAL, false)
        binding.RefreshLayout.setHeaderMaxDragRate(4F)
        binding.RefreshLayout.setOnRefreshListener { refreshLayout ->
            for (i in 0 until alphaAdapter.itemCount) {
                alphaAdapter.notifyItemChanged(i)
            }
            refreshLayout.finishRefresh()
        }
    }

    private fun initbtn() {
        StaticData.SelectID = 0
        binding.tv1.setOnClickListener {
            StaticData.SelectID = 0
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 0
        }
        binding.tv2.setOnClickListener {
            StaticData.SelectID = 1
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 1
        }
        binding.tv3.setOnClickListener {
            StaticData.SelectID = 2
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 2
        }
        binding.tv4.setOnClickListener {
            StaticData.SelectID = 3
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 3
        }
        binding.tv5.setOnClickListener {
            StaticData.SelectID = 4
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 4
        }
        btn_group.add(binding.btn1)
        btn_group.add(binding.btn2)
        btn_group.add(binding.btn3)
        btn_group.add(binding.btn4)
        btn_group.add(binding.btn5)
        tv_group.add(binding.tv1)
        tv_group.add(binding.tv2)
        tv_group.add(binding.tv3)
        tv_group.add(binding.tv4)
        tv_group.add(binding.tv5)
    }

    private fun setbtn(last_SelectID: Int, SelectID: Int) {
        btn_group[last_SelectID]?.visibility = View.GONE
        btn_group[SelectID]?.visibility = View.VISIBLE
        tv_group[last_SelectID]?.setTextColor(Color.parseColor("#FF6399fd"))
        tv_group[SelectID]?.setTextColor(Color.parseColor("#FFFFFF"))
    }

    override fun onResume() {
        super.onResume()
        if (StaticData.Songs != null) {
            if (MediaPlayerHelper.getInstance(this@SongListActivity)?.isPlaying == true) {
                binding.PlayButton.setBackgroundResource(R.drawable.play_pressed)
            } else {
                binding.PlayButton.setBackgroundResource(R.drawable.play)
            }
            binding.PlayBar.visibility = View.VISIBLE
            binding.Avatar2.visibility = View.VISIBLE
            binding.Avatar2.startAnimation(animation)
            binding.BarName.setText(StaticData.Songs?.getName())
            Glide.with(this)
                .load(StaticData.Songs?.getCover_url() + "?param=200y200")
                .into(binding.Avatar2)
        }
    }

    override fun finish() {
        super.finish()
        pools.shutdown()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}