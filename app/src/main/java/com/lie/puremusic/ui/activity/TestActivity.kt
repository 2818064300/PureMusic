package com.lie.puremusic.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lie.puremusic.databinding.ActivityTestBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject


class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var url:String? = null
        Thread {
            var time = System.currentTimeMillis()
            println(time)
            val request: Request = Request.Builder()
                .url("http://www.puremusic.com.cn:3000/login/qr/key?" + "timerstamp=" + time)
                .build()
            var response: Response? = OkHttpClient().newCall(request).execute()
            var key: String =
                JSONObject(response?.body?.string()).getJSONObject("data").getString("unikey")
            println(key)
            val request2: Request = Request.Builder()
                .url("http://www.puremusic.com.cn:3000/login/qr/create?key=" + key + "&qrimg=true&timerstamp=" + time)
                .build()
            var response2: Response? = OkHttpClient().newCall(request2).execute()
            url = JSONObject(response2?.body?.string()).getJSONObject("data").getString("qrurl")
            println(url)
        }.start()
        Glide.with(this)
            .load(url)
            .into(binding.qr)
    }
}