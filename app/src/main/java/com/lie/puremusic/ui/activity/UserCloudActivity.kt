package com.lie.puremusic.ui.activity

import android.graphics.Typeface
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivityUserCloudBinding
import com.lie.puremusic.music.netease.data.toStandard
import com.lie.puremusic.ui.base.BaseActivity
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class UserCloudActivity : BaseActivity() {
    private lateinit var binding: ActivityUserCloudBinding

    override fun initBinding() {
        binding = ActivityUserCloudBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
    }
    override fun initView(){
        val adapter = MyRecyclerAdapter(
            this,
            StaticData.UserCloudData?.data?.toStandard(),
            "UserCloud"
        )
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.rvUserCloudSong.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.rvUserCloudSong.layoutManager =
            LinearLayoutManager(this@UserCloudActivity, LinearLayoutManager.VERTICAL, false)
        val typeface = Typeface.createFromAsset(this.assets, "汉仪雅酷黑65W.ttf")
        binding.tvUserCloud.setTypeface(typeface)
    }
}