package com.lie.puremusic.utils

import android.content.Context
import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.SongList
import com.lie.puremusic.pojo.User
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


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
        if (sharedPreference?.contains("id") != true) {
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
            StaticData.user?.setMusic_id(sharedPreference.getString("music_id", null))
            StaticData.user?.setName(sharedPreference.getString("name", null))
            StaticData.user?.setAvatarUrl(sharedPreference.getString("avatarUrl", null))
        }
        StaticData.User = User(StaticData.user?.getId())
        val pools = Executors.newCachedThreadPool()
        pools.submit() {
            val request: Request = Request.Builder()
                .url("http://www.puremusic.com.cn:3000/user/detail?uid=" + StaticData.user?.getMusic_id())
                .build()
            val response: Response? = OkHttpClient().newCall(request).execute()
            val User = response?.body?.string()?.let { JSONObject(it) }
            //获取登录用户信息
            if (User != null) {
                StaticData.User?.setIdentify(User.getJSONObject("identify").getString("imageDesc"))
                StaticData.User?.setLevel(User.getInt("level"))
                StaticData.User?.setListenSongs(User.getInt("listenSongs"))
            }
        }
        pools.submit() {
            val request: Request = Request.Builder()
                .url("http://www.puremusic.com.cn:3000/user/playlist?uid=" + StaticData.user?.getMusic_id())
                .addHeader("cookie", StaticData.cookie)
                .build()
            val response: Response? = OkHttpClient().newCall(request).execute()
            val playlists =
                response?.body?.string()?.let { JSONObject(it).getJSONArray("playlist") }
            if (playlists != null) {
                StaticData.User?.setFavorite(SongList(playlists.getJSONObject(0).getString("id")))
                StaticData.User?.getFavorite()
                    ?.setName(playlists.getJSONObject(0).getString("name"))
                StaticData.User?.getFavorite()
                    ?.setCover_url(playlists.getJSONObject(0).getString("coverImgUrl"))
                es.submit(
                    GetSonglistData2(
                        StaticData.User?.getFavorite(),
                        playlists.getJSONObject(0),
                        true
                    )
                )
                //遍历用户歌单(除去我喜欢的音乐)
                for (i in 1 until playlists.length()) {
                    StaticData.User?.getSongLists()
                        ?.add(SongList(playlists.getJSONObject(i).getString("id")))
                    StaticData.User?.getSongLists()?.get(i - 1)
                        ?.setName(playlists.getJSONObject(i).getString("name"))
                    StaticData.User?.getSongLists()?.get(i - 1)
                        ?.setCover_url(playlists.getJSONObject(i).getString("coverImgUrl"))
                    es.submit(
                        GetSonglistData2(
                            StaticData.User?.getSongLists()?.get(i - 1),
                            playlists.getJSONObject(i),
                            true
                        )
                    )
                }
            }
            es.shutdown()
        }
        es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
    }
}