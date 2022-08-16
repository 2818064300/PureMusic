package com.lie.puremusic.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ktx.immersionBar
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivitySingerBinding
import com.lie.puremusic.listener.AppBarStateChangeListener
import com.lie.puremusic.pojo.Singer
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors

class SingerActivity : AppCompatActivity(){
    private var Singer: Singer? = null
    private lateinit var binding: ActivitySingerBinding
    private val pools = Executors.newCachedThreadPool()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        immersionBar {
            statusBarColor(R.color.nullcolor)
            statusBarDarkFont(true)
        }

        binding.RadioButtonFirst.isChecked = true
        when (intent.getStringExtra("style")) {
            "SingerList" -> Singer = StaticData.Home.getSingers()?.get(intent.getIntExtra("index", Int.MAX_VALUE))
            "SearchSinger" -> Singer = StaticData.Result.getSingers()?.get(intent.getIntExtra("index", Int.MAX_VALUE))
            "Singer_ID" -> Singer = StaticData.Singer
        }
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        binding.RefreshLayout.autoRefresh(200)
        val d = Date()
        val hours = d.hours
        if (hours < 6) {
            binding.Tips.setText("夜深了,月亮都睡了")
        }
        if (hours >= 6) {
            binding.Tips.setText("早上好鸭,Lie.")
        }
        if (hours >= 8) {
            binding.Tips.setText("今天要元气满满哦")
        }
        if (hours >= 12) {
            binding.Tips.setText("不喝杯下午茶吗?")
        }
        if (hours >= 18) {
            binding.Tips.setText("傍晚该放松一下喽")
        }
        if (hours >= 22) {
            binding.Tips.setText("夜深了,请注意休息")
        }
        binding.FAB.setOnClickListener(View.OnClickListener { binding.RefreshLayout.autoRefresh() })
        binding.FAB.setOnLongClickListener(OnLongClickListener {
            binding.SingerRv.smoothScrollToPosition(0)
            false
        })
        binding.FAB.setVisibility(View.GONE)
        binding.MaterialToolbar.setTitle(Singer?.getName())
        binding.CenterAppbarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state === State.EXPANDED) {
                    binding.Container.setVisibility(View.VISIBLE)
                    binding.Container2.setVisibility(View.GONE)
                    binding.Container3.setVisibility(View.VISIBLE)
                    binding.FAB.setVisibility(View.GONE)
                } else if (state === State.COLLAPSED) {
                    binding.Container.setVisibility(View.GONE)
                    binding.Container2.setVisibility(View.VISIBLE)
                    binding.Container3.setVisibility(View.GONE)
                    binding.FAB.setVisibility(View.VISIBLE)
                } else {
                    binding.Container.setVisibility(View.VISIBLE)
                    binding.Container2.setVisibility(View.GONE)
                    binding.Container3.setVisibility(View.VISIBLE)
                    binding.FAB.setVisibility(View.GONE)
                }
            }
        })
        if (Singer?.getCover_url() != null) {
            Glide.with(this@SingerActivity)
                .load(Singer?.getCover_url() + "?param=200y200")
                .into(binding.Avatar)
        } else {
            Glide.with(this@SingerActivity)
                .load(R.drawable.avatar)
                .into(binding.Avatar)
        }
        binding.SingerName.setText(Singer?.getName())
        binding.SingerStyle.setText("华语歌手")

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
                                if(Songs?.getSingers()?.isEmpty() == true) {
                                    for (i in 0 until songs.getJSONObject(0).getJSONArray("ar")
                                        .length()) {
                                        val singer = Singer(
                                            songs.getJSONObject(0).getJSONArray("ar")
                                                .getJSONObject(i)
                                                .getString("id")
                                        )
                                        singer.setName(
                                            songs.getJSONObject(0).getJSONArray("ar")
                                                .getJSONObject(i)
                                                .getString("name")
                                        )
                                        Songs?.getSingers()?.add(singer)
                                    }
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
                if(j>StaticData.CacheValue){
                    Thread.sleep(50)
                }
            }
            pools.shutdown()
        }.start()


        val adapter = MyRecyclerAdapter(this, StaticData.PlayList)
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.SingerRv.setAdapter(AlphaInAnimationAdapter(alphaAdapter))
        binding.SingerRv.setLayoutManager(
            LinearLayoutManager(
                this@SingerActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        binding.RefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            for (i in 0 until alphaAdapter.itemCount) {
                alphaAdapter.notifyItemChanged(i)
            }
            if (Singer?.getCover_url() != null) {
                Glide.with(this@SingerActivity)
                    .load(Singer?.getCover_url() + "?param=200y200")
                    .into(binding.Avatar)
            } else {
                Glide.with(this@SingerActivity)
                    .load(R.drawable.avatar)
                    .into(binding.Avatar)
            }
            refreshLayout.finishRefresh()
        })
    }

    override fun finish() {
        super.finish()
        pools.shutdown()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}