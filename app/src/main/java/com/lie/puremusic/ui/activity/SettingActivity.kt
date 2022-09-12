package com.lie.puremusic.ui.activity

import android.content.Context
import androidx.core.view.WindowCompat
import com.lie.puremusic.StaticData.Companion.mmkv
import com.lie.puremusic.databinding.ActivitySettingBinding
import com.lie.puremusic.ui.base.BaseActivity
class SettingActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingBinding
    private var mExitTime: Long = 0
    companion object {
        lateinit var context: Context
    }

    override fun initBinding() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
        context = this
    }

    override fun initView() {
        // 按钮
        binding.apply {
            switcherHires.setChecked(mmkv.decodeBool("Hires", true))
            switcherPlayMode.setChecked(mmkv.decodeBool("PlayMode", true))
            switcherQuickSearch.setChecked(mmkv.decodeBool("QuickSearch", true))
            switcherAutoRefresh.setChecked(mmkv.decodeBool("AutoRefresh", true))
        }
    }
    override fun initListener() {
        binding.apply {
            switcherHires.setOnCheckedChangeListener {
                mmkv.encode(
                    "Hires",
                    it
                )
            }
            switcherPlayMode.setOnCheckedChangeListener {
                mmkv.encode(
                    "PlayMode",
                    it
                )
            }
            switcherQuickSearch.setOnCheckedChangeListener {
                mmkv.encode(
                    "QuickSearch",
                    it
                )
            }
            switcherAutoRefresh.setOnCheckedChangeListener {
                mmkv.encode(
                    "AutoRefresh",
                    it
                )
            }
        }
    }
}