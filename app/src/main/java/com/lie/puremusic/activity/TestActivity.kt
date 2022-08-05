package com.lie.puremusic.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lie.puremusic.databinding.ActivityTestBinding
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class TestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btn1.setOnClickListener {
            var url = "http://www.puremusic.com.cn:3000/song/detail?ids=29774171";
            val request: Request = Request.Builder()
                .url(url)
                .method("GET", null)
                .build()

            OkHttpClient().newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var name = JSONObject(response.body?.string()).getJSONArray("songs").getJSONObject(0).getString("name")
                        runOnUiThread {
                            binding.tv1.text = name
                        }
                    }
                })
        }
    }
}