package com.lie.puremusic.activity

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivityRecommendBinding
import com.lie.puremusic.databinding.ActivityUserCloudBinding
import com.lie.puremusic.music.netease.data.toStandard
import com.lie.puremusic.music.netease.data.toStandardSongDataArrayList
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class UserCloudActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserCloudBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCloudBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        val adapter = MyRecyclerAdapter(
            this,
            StaticData.UserCloudData?.data?.toStandard()
        )
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.rvUserCloudSong.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.rvUserCloudSong.layoutManager =
            LinearLayoutManager(this@UserCloudActivity, LinearLayoutManager.VERTICAL, false)
        val typeface = Typeface.createFromAsset(this.assets, "汉仪雅酷黑65W.ttf")
        binding.tvUserCloud.setTypeface(typeface)
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}