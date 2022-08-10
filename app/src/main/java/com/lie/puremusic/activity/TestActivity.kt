package com.lie.puremusic.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityTestBinding
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
                                            var url =
                                                "http://www.puremusic.com.cn:3000/login/qr/check?key=" + key + "&timerstamp=" + System.currentTimeMillis()
                                            val request: Request = Request.Builder()
                                                .url(url)
                                                .method("GET", null)
                                                .build()

                                            OkHttpClient().newCall(request)
                                                .enqueue(object : Callback {
                                                    override fun onFailure(
                                                        call: Call,
                                                        e: IOException
                                                    ) {
                                                    }

                                                    override fun onResponse(
                                                        call: Call,
                                                        response: Response
                                                    ) {
                                                        println(response.body?.string())
                                                    }
                                                })
                                        }
                                    }, 0, 3000)
                                }
                            })
                    }
                })
        }
        binding.btn2.setOnClickListener {
            Thread {
                StaticData.jedis = RedisUtils.getConnection()
                var cookie = "NMTID=00OABGkx53WhxqYPkwKokFu94D5p3QAAAGChdig5Q; Max-Age=315360000; Expires=Sat, 07 Aug 2032 03:42:22 GMT; Path=/;;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_U=8de9acde979fb024e459d41dee8a23ea5d7c2b1b5619dad8e72d1493644457008a08bd5bf851808fe21b68546e8b351ccade798774a697593650ddea49e375e59689f7abbb96f608d4dbf082a8813684; Max-Age=15552000; Expires=Mon, 06 Feb 2023 03:42:22 GMT; Path=/; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/feedback; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/openapi/clientlog; HTTPOnly;__csrf=31f6c5c5f7f0453332eef862ccf5f67c; Max-Age=1296010; Expires=Thu, 25 Aug 2022 03:42:32 GMT; Path=/;;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_SNS=; Max-Age=0; Expires=Wed, 10 Aug 2022 03:42:22 GMT; Path=/;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/openapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/clientlog; HTTPOnly"
                StaticData.jedis?.set("cookie",cookie)
                println("保存成功")
            }.start()
        }
    }
}