package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.UserCloudData
import com.lie.puremusic.utils.MagicHttp
import okhttp3.FormBody

/**
 * 用户云盘数据
 */
object UserCloud {

    fun getUserCloud(success: (UserCloudData) -> Unit) {
        val requestBody = FormBody.Builder()
            .add("crypto", "api")
            .add("cookie", StaticData.cookie)
            .add("withCredentials", "true")
            .add("realIP", "211.161.244.70")
            .add("limit", "50")
            .build()
        MagicHttp.OkHttpManager().newPost("${API_DEFAULT}/user/cloud", requestBody, {
            try {
                val userCloudData = Gson().fromJson(it, UserCloudData::class.java)
                success.invoke(userCloudData)
            } catch (e: Exception) {
            }
        }, {

        })
    }

}