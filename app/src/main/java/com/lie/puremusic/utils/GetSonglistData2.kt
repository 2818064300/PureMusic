package com.lie.puremusic.utils

import com.lie.puremusic.pojo.SongList
import org.json.JSONObject

class GetSonglistData2 :Runnable{
    private var jsonObject: JSONObject? = null
    private var SongList: SongList? = null
    private var NoBook = false

    constructor(SongList: SongList?, jsonObject: JSONObject?) {
        this.SongList = SongList
        this.jsonObject = jsonObject
    }

    constructor(SongList: SongList?, jsonObject: JSONObject?, NoBook: Boolean) {
        this.SongList = SongList
        this.jsonObject = jsonObject
        this.NoBook = NoBook
    }

    override fun run() {
        SongList?.setId(jsonObject?.getString("id"))
        SongList?.setName(jsonObject?.getString("name"))
        SongList?.setCreator_name(jsonObject?.getJSONObject("creator")?.getString("nickname"))
        SongList?.setCover_url(jsonObject?.getString("coverImgUrl"))
        jsonObject?.let { SongList?.setPlayCount(it.getInt("playCount")) }
        jsonObject?.getInt("trackCount")?.let { SongList?.setCount(it) }
        if (!NoBook) {
            SongList?.setBookCount(jsonObject!!.getInt("bookCount"))
        }
    }
}