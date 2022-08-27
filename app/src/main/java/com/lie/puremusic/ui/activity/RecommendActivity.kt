package com.lie.puremusic.ui.activity

import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivityRecommendBinding
import com.lie.puremusic.databinding.ActivityUserCloudBinding
import com.lie.puremusic.music.netease.data.toStandard
import com.lie.puremusic.music.netease.data.toStandardSongDataArrayList
import com.lie.puremusic.ui.base.BaseActivity
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.util.*

class RecommendActivity : BaseActivity() {
    private lateinit var binding: ActivityRecommendBinding
    override fun initBinding() {
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
    }
    override fun initView(){
        val adapter = MyRecyclerAdapter(
            this,
            StaticData.DailyRecommendSongData?.data?.dailySongs?.toStandardSongDataArrayList(),
            "DailyRecommend"
        )
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.rvRecommendSong.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.rvRecommendSong.layoutManager =
            LinearLayoutManager(this@RecommendActivity, LinearLayoutManager.VERTICAL, false)
        binding.tvDate.text = String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        binding.tvMonth.text = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1)
    }

}