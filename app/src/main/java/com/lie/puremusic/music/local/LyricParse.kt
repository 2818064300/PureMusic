package com.lie.puremusic.music.local

import com.lie.puremusic.music.qq.SearchSong
import com.lie.puremusic.standard.SearchLyric

/**
 * 歌词适配
 * 为本地音乐添加来自网络的歌词
 */
object LyricParse {

    /**
     * 获取网络歌词
     * 传入名称
     */
    fun getLyric(name: String, success: (String) -> Unit) {
            // 调用一次 QQ 搜索
            SearchSong.search(name) {
                if (it.isNotEmpty()) {
                    SearchLyric.getLyricString(it[0]) { string ->
                        success.invoke(string)
                    }
                } else {
                    success.invoke("")
                }
            }
    }

}