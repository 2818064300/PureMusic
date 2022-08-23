package com.lie.puremusic.music.netease

import android.util.Log
import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.CommentData
import com.lie.puremusic.utils.MagicHttp
import com.lie.puremusic.utils.getCurrentTime

object Comment {

    private fun timestamp(): String {
        return "&timestamp=${getCurrentTime()}"
    }
    fun getComment(id: String, success: (CommentData) -> Unit) {
        val url =
            "$API_DEFAULT/comment/music?id=${id}&limit=20&offset=0${timestamp()}&cookie=${StaticData.cookie}"
        MagicHttp.OkHttpManager().newGet(url, {
            try {
                val commentData = Gson().fromJson(it, CommentData::class.java)
                success(commentData)
            } catch (e: Exception) {
            }
        }, {

        })
    }
}