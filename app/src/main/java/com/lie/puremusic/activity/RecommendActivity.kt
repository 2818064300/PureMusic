package com.lie.puremusic.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivityRecommendBinding
import com.lie.puremusic.music.netease.data.toStandardSongDataArrayList
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.util.*

class RecommendActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        val adapter = MyRecyclerAdapter(
            this,
            StaticData.DailyRecommendSongData?.data?.dailySongs?.toStandardSongDataArrayList()
        )
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.rvRecommendSong.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.rvRecommendSong.layoutManager =
            LinearLayoutManager(this@RecommendActivity, LinearLayoutManager.VERTICAL, false)
        binding.tvDate.text = String.format("%02d", Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        binding.tvMonth.text = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1)
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}