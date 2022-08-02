package com.lie.puremusic.utils

import com.lie.puremusic.pojo.Song
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

class GetQQpicurl : Runnable{
    private var songs: Song? = null
    private var id: String? = null

    constructor(songs: Song?, id: String?) {
        this.songs = songs
        this.id = id
    }

    override fun run() {
        try {
            val document: Document = Jsoup.connect("https://y.qq.com/n/ryqq/songDetail/$id")
                .ignoreContentType(true)
                .get()
            val str = "" + document.getElementsByTag("script").eq(5)
            println(str)
            songs?.setCover_url(
                "http://y.qq.com/music/photo_new/" + str.substring(
                    str.indexOf(
                        "u002F",
                        str.indexOf(
                            "u002F",
                            str.indexOf(
                                "u002F",
                                str.indexOf("u002F", str.indexOf("u002F") + 4) + 4
                            ) + 4
                        ) + 4
                    ) + 5, str.indexOf("?max_age")
                )
            )
            println(
                "http://y.qq.com/music/photo_new/" + str.substring(
                    str.indexOf(
                        "u002F",
                        str.indexOf(
                            "u002F",
                            str.indexOf(
                                "u002F",
                                str.indexOf("u002F", str.indexOf("u002F") + 4) + 4
                            ) + 4
                        ) + 4
                    ) + 5, str.indexOf("?max_age")
                )
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}