package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.PlaylistDetail
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit
import com.lie.puremusic.standard.data.StandardSongData

/**
 * 获取网易云歌单全部，对于大型歌单也要成功
 */
object Playlist {

    private const val PLAYLIST_URL = "${API_DEFAULT}/playlist/track/all?id=" // 获取歌单链接
    private const val SINGER_URL = "${API_DEFAULT}/artist/top/song?id=" // 获取歌单链接

    /**
     * 网易云登录成功的用户通过这个接口获取歌单歌曲
     */
    fun getPlaylistByCookie(playlistId: Long, success: (ArrayList<StandardSongData>) -> Unit) {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build()

            val request: Request = Request.Builder()
                .url("${PLAYLIST_URL}${playlistId}")
                .addHeader("cookie", StaticData.cookie)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    response.body?.byteStream()?.let { inputStream ->
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        var playlistDetail: PlaylistDetail? = null
                        try {
                            playlistDetail = Gson().fromJson(reader, PlaylistDetail::class.java)
                        } catch (e: Exception) {

                        }
                        playlistDetail?.getSongArrayList()?.let { success(it) }
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

    fun getSingerByCookie(playlistId: Long, success: (ArrayList<StandardSongData>) -> Unit) {
        try {
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build()

            val request: Request = Request.Builder()
                .url("${SINGER_URL}${playlistId}")
                .addHeader("cookie", StaticData.cookie)
                .get()
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    response.body?.byteStream()?.let { inputStream ->
                        val reader = BufferedReader(InputStreamReader(inputStream))
                        var playlistDetail: PlaylistDetail? = null
                        try {
                            playlistDetail = Gson().fromJson(reader, PlaylistDetail::class.java)
                        } catch (e: Exception) {

                        }
                        playlistDetail?.getSongArrayList()?.let { success(it) }
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