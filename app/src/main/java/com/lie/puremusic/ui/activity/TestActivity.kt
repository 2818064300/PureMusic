package com.lie.puremusic.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityTestBinding
import com.lie.puremusic.music.netease.Playlist
import com.lie.puremusic.standard.SearchLyric
import com.lie.puremusic.utils.RedisUtils
import okhttp3.*
import org.json.Cookie
import org.json.JSONObject
import java.io.IOException
import java.util.*


class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}