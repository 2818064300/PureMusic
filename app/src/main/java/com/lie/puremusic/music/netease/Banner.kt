package com.lie.puremusic.music.netease

import com.google.gson.Gson
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.BannerData
import com.lie.puremusic.utils.MagicHttp

object Banner {
    private const val url = "$API_DEFAULT/banner"
    fun getBanner(success: (BannerData) -> Unit) {
        MagicHttp.OkHttpManager().newGet(url, {
            try {
                val BannerData = Gson().fromJson(it, BannerData::class.java)
                if (BannerData.code == 200) {
                    success.invoke(BannerData)
                }
            } catch (e: Exception) {

            }
        }, {

        })
    }
}