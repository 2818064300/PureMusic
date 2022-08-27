package com.lie.puremusic.ui.activity

import android.view.View
import android.view.View.OnLongClickListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ktx.immersionBar
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivitySingerBinding
import com.lie.puremusic.listener.AppBarStateChangeListener
import com.lie.puremusic.standard.data.StandardSingerData
import com.lie.puremusic.ui.base.BaseActivity
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.util.*

class SingerActivity : BaseActivity(){
    private lateinit var binding: ActivitySingerBinding
    private var SingerData: StandardSingerData? = null

    override fun initBinding() {
        binding = ActivitySingerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
    }
    override fun initData() {
        SingerData = StaticData.SingerData
    }
    override fun initView() {
        immersionBar {
            statusBarColor(R.color.nullcolor)
            statusBarDarkFont(true)
        }
        binding.RadioButtonFirst.isChecked = true
        binding.RefreshLayout.autoRefresh(200)
        val hours = Date().hours
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
        if (SingerData?.picUrl != null) {
            Glide.with(this@SingerActivity)
                .load(SingerData?.picUrl + "?param=200y200")
                .into(binding.Avatar)
            Glide.with(this@SingerActivity)
                .load(SingerData?.picUrl + "?param=200y200")
                .into(binding.ivCover)
        } else {
            Glide.with(this@SingerActivity)
                .load(R.drawable.avatar)
                .into(binding.Avatar)
            Glide.with(this@SingerActivity)
                .load(R.drawable.avatar)
                .into(binding.ivCover)
        }
        binding.SingerName.setText(SingerData?.name)
        binding.SingerStyle.setText("华语歌手")
        val adapter = MyRecyclerAdapter(this, SingerData?.songs,"SingerList")
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
            if (SingerData?.picUrl != null) {
                Glide.with(this@SingerActivity)
                    .load(SingerData?.picUrl + "?param=200y200")
                    .into(binding.Avatar)
                Glide.with(this@SingerActivity)
                    .load(SingerData?.picUrl + "?param=200y200")
                    .into(binding.ivCover)
            } else {
                Glide.with(this@SingerActivity)
                    .load(R.drawable.avatar)
                    .into(binding.Avatar)
                Glide.with(this@SingerActivity)
                    .load(R.drawable.avatar)
                    .into(binding.ivCover)
            }
            for (i in 0 until alphaAdapter.itemCount) {
                alphaAdapter.notifyItemChanged(i)
            }
            refreshLayout.finishRefresh()
        })
        binding.tvTitle.text = SingerData?.name
    }
    override fun initListener(){
        binding.CenterAppbarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state === State.EXPANDED) {
                    binding.Container.setVisibility(View.VISIBLE)
                    binding.Container2.setVisibility(View.GONE)
                    binding.Container3.setVisibility(View.VISIBLE)
                } else if (state === State.COLLAPSED) {
                    binding.Container.setVisibility(View.GONE)
                    binding.Container2.setVisibility(View.VISIBLE)
                    binding.Container3.setVisibility(View.GONE)
                } else {
                    binding.Container.setVisibility(View.VISIBLE)
                    binding.Container2.setVisibility(View.GONE)
                    binding.Container3.setVisibility(View.VISIBLE)
                }
            }
        })
    }
}