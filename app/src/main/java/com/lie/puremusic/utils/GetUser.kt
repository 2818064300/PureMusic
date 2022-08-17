package com.lie.puremusic.utils

import android.content.Context
import com.lie.puremusic.StaticData
import com.lie.puremusic.music.netease.UserDetail
import com.lie.puremusic.music.netease.UserPlaylist
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.Executors


class GetUser : Runnable {
    private var account: String? = null
    private var password: String? = null
    private var context: Context? = null
    private val es = Executors.newCachedThreadPool()

    constructor(account: String?, password: String?, context: Context) {
        this.account = account
        this.password = password
        this.context = context
    }

    override fun run() {
        val sharedPreference =
            context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (sharedPreference?.contains("id") == true) {
            //登录
            val request: Request = Request.Builder()
                .url("http://www.puremusic.com.cn:3000/login/cellphone?phone=$account&password=$password")
                .build()
            //刷新登录
            val refresh: Request = Request.Builder()
                .url("http://www.puremusic.com.cn:3000/login/refresh")
                .build()
            val response = OkHttpClient().newCall(request).execute()
            val jsonObject = response.body?.string()?.let { JSONObject(it) }
            StaticData.user?.setMusic_id(jsonObject?.getJSONObject("account")?.getString("id"))
            StaticData.user?.setName(jsonObject?.getJSONObject("profile")?.getString("nickname"))
            StaticData.user?.setAvatarUrl(
                jsonObject?.getJSONObject("profile")?.getString("avatarUrl")
            )
            val editor = sharedPreference?.edit()
            editor?.putString("id", StaticData.user?.getId())
            editor?.putString("music_id", StaticData.user?.getMusic_id())
            editor?.putString("name", StaticData.user?.getName())
            editor?.putString("avatarUrl", StaticData.user?.getAvatarUrl())
            editor?.putString("account", StaticData.user?.getAccount())
            editor?.putString("password", StaticData.user?.getPassword())
            editor?.apply()
        } else {
            StaticData.user?.setMusic_id(sharedPreference?.getString("music_id", "1333576013"))
            StaticData.user?.setName(sharedPreference?.getString("name", "执意画红尘z"))
            StaticData.user?.setAvatarUrl(sharedPreference?.getString("avatarUrl", "https://p3.music.126.net/M4jrGsicytQjxCoWQA_rVg==/109951165806189999.jpg"))


        }
    }
}