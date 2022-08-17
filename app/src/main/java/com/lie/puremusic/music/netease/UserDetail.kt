package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.UserDetailData
import com.lie.puremusic.utils.MagicHttp

/**
 * 获取网用户信息
 */
object UserDetail {

    fun getUserDetail(context: Context, success: (UserDetailData) -> Unit) {

        val url = "${API_DEFAULT}/user/detail?uid=" + "1333576013"

        MagicHttp.OkHttpManager().getByCache(context, url, {
            try {
                val UserDetailData = Gson().fromJson(it, UserDetailData::class.java)
                success.invoke(UserDetailData)
            } catch (e: Exception) {
            }
        }, {
        })

    }
}


