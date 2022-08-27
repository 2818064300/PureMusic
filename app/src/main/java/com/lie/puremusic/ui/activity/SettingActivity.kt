package com.lie.puremusic.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gyf.immersionbar.ktx.immersionBar
import com.lie.puremusic.R
import com.lie.puremusic.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        immersionBar {
            statusBarColor(R.color.nullcolor)
            statusBarDarkFont(true)
        }
    }
}