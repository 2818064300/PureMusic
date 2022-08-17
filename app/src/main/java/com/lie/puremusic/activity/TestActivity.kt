package com.lie.puremusic.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityTestBinding
import com.lie.puremusic.music.netease.Playlist
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
        binding.btn1.setOnClickListener {
            println(System.currentTimeMillis())
            var url =
                "http://www.puremusic.com.cn:3000/login/qr/key?timerstamp=" + System.currentTimeMillis()
            val request: Request = Request.Builder()
                .url(url)
                .method("GET", null)
                .build()

            OkHttpClient().newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var key = JSONObject(response.body?.string()).getJSONObject("data")
                            .getString("unikey")
                        var url =
                            "http://www.puremusic.com.cn:3000/login/qr/create?key=${key}&qrimg=true&timerstamp=" + System.currentTimeMillis()
                        val request: Request = Request.Builder()
                            .url(url)
                            .method("GET", null)
                            .build()

                        OkHttpClient().newCall(request)
                            .enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    var qrimg =
                                        JSONObject(response.body?.string()).getJSONObject("data")
                                            .getString("qrimg")
                                    runOnUiThread {
                                        Glide.with(this@TestActivity)
                                            .load(qrimg)
                                            .into(binding.img1)
                                    }
                                    Timer().schedule(object : TimerTask() {
                                        override fun run() {
                                            var url = "http://www.puremusic.com.cn:3000/login/qr/check?key=" + key + "&timerstamp=" + System.currentTimeMillis()
                                            println(url)
//                                            val request: Request = Request.Builder()
//                                                .url(url)
//                                                .method("GET", null)
//                                                .build()
//
//                                            OkHttpClient().newCall(request)
//                                                .enqueue(object : Callback {
//                                                    override fun onFailure(
//                                                        call: Call,
//                                                        e: IOException
//                                                    ) {
//                                                    }
//
//                                                    override fun onResponse(
//                                                        call: Call,
//                                                        response: Response
//                                                    ) {
//                                                        println(response.body?.string())
//                                                    }
//                                                })
                                        }
                                    }, 0, 3000)
                                }
                            })
                    }
                })
        }
        binding.btn2.setOnClickListener {
        }
    }
}