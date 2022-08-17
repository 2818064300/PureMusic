package com.lie.puremusic.music.netease

import android.content.Context
import com.google.gson.Gson
import com.lie.puremusic.StaticData
import com.lie.puremusic.api.API_DEFAULT
import com.lie.puremusic.music.netease.data.SearchResult
import com.lie.puremusic.music.netease.data.UserDetailData
import com.lie.puremusic.music.netease.data.UserPlaylistData
import com.lie.puremusic.utils.MagicHttp
import okhttp3.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

/**
 * 获取网用户信息
 */
object SearchDetail {

    fun getSearchResult(context: Context, success: (SearchResult) -> Unit) {
        var url = "http://www.puremusic.com.cn:3000/cloudsearch?keywords=${StaticData.KeyWords}&type=1"
        when (StaticData.SelectID) {
            0 -> url =
                "http://www.puremusic.com.cn:3000/cloudsearch?keywords=${StaticData.KeyWords}&type=1"
            2 -> url =
                "http://www.puremusic.com.cn:3000/cloudsearch?keywords=${StaticData.KeyWords}&type=100"
            3 -> url =
                "http://www.puremusic.com.cn:3000/cloudsearch?keywords=${StaticData.KeyWords}&type=1000"
            4 -> url =
                "http://www.puremusic.com.cn:3000/cloudsearch?keywords=${StaticData.KeyWords}&type=1006"
        }

        MagicHttp.OkHttpManager().getByCache(context, url, {
            try {
                val SearchResult = Gson().fromJson(it, SearchResult::class.java)
                success.invoke(SearchResult)
            } catch (e: Exception) {
            }
        }, {
        })

    }
}


