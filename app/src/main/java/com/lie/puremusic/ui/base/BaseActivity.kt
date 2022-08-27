package com.lie.puremusic.ui.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.MiniPlayerBinding
import com.lie.puremusic.manager.ActivityCollector
import com.lie.puremusic.ui.activity.PlayerActivity

/**
 * 基类 Activity
 */
abstract class BaseActivity : AppCompatActivity() {
    var miniPlayer: MiniPlayerBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        initBinding()
        initData()
        initView()
        initListener()
        initObserver()
        initBroadcastReceiver()
        initMiniPlayer()
    }

    override fun onStart() {
        super.onStart()
        initShowDialogListener()
    }

    @SuppressLint("SetTextI18n")
    private fun initMiniPlayer() {
        miniPlayer?.let { mini ->
            mini.apply {
                PlayBar.visibility = View.GONE
                ivCover.visibility = View.GONE
                root.setOnClickListener {
                    startActivity(Intent(this@BaseActivity, PlayerActivity::class.java))
                }
                PlayButton.setOnClickListener {
                    if (PlayerActivity.mediaPlayerHelper?.isPlaying == true) {
                        PlayerActivity.mediaPlayerHelper?.pause()
                        PlayButton.setBackgroundResource(R.drawable.play)
                    } else {
                        PlayerActivity.mediaPlayerHelper?.start()
                        PlayButton.setBackgroundResource(R.drawable.play_pressed)
                    }
                }
            }
        }
    }
    protected open fun initBinding() {}

    protected open fun initView() {}

    protected open fun initData() {}

    protected open fun initListener() {}

    protected open fun initObserver() {}

    protected open fun initBroadcastReceiver() {}

    protected open fun initShowDialogListener() {}

    override fun onResume() {
        super.onResume()
        var animation: Animation? = AnimationUtils.loadAnimation(this, R.anim.img_animation)
        animation?.interpolator = LinearInterpolator()
        miniPlayer?.let { mini ->
            mini.apply {
                if (StaticData.Songs != null) {
                    if (PlayerActivity.mediaPlayerHelper?.isPlaying == true) {
                        PlayButton.setBackgroundResource(R.drawable.play_pressed)
                    } else {
                        PlayButton.setBackgroundResource(R.drawable.play)
                    }
                    PlayBar.visibility = View.VISIBLE
                    ivCover.visibility = View.VISIBLE
                    ivCover.startAnimation(animation)
                    tvTitle.setText(StaticData.Songs?.name)
                    Glide.with(this@BaseActivity)
                        .load(StaticData.Songs?.imageUrl)
                        .into(ivCover)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}