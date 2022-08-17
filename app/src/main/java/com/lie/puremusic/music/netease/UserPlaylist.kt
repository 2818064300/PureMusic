package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.PlaylistDetail
import com.lie.puremusic.music.netease.data.UserPlaylistData
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import com.lie.puremusic.standard.data.StandardSongData

/**
 * 获取网易云歌单全部，对于大型歌单也要成功
 */
object UserPlaylist {

    private const val USER_URL = "${API_DEFAULT}/user/playlist?uid=" // 获取歌单链接

    fun getUserByCookie(userId: Long, success: (ArrayList<UserPlaylistData.Playlist>) -> Unit) {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build()

            val request: Request = Request.Builder()
                .url("${USER_URL}${userId}")
                .addHeader("cookie", StaticData.cookie)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    response.body?.byteStream()?.let { inputStream ->
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        var userPlaylistData: UserPlaylistData? = null
                        try {
                            userPlaylistData = Gson().fromJson(reader, UserPlaylistData::class.java)
                            success.invoke(userPlaylistData.playlist)
                        } catch (e: Exception) {

                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    // failure(ErrorCode.ERROR_MAGIC_HTTP)
                }
            })
        } catch (e: Exception) {
            // failure(ErrorCode.ERROR_MAGIC_HTTP)
        }
    }
}