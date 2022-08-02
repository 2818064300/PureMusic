package com.lie.puremusic.utils

import com.lie.puremusic.StaticData

class GetSingerData2 : Runnable{
    private var i = 0
    private var id: String? = null
    private var name: String? = null
    private var cover_url: String? = null

    constructor(i: Int, id: String?, name: String?, cover_url: String) {
        this.i = i
        this.id = id
        this.name = name
        this.cover_url = cover_url
    }

    override fun run() {
        StaticData.Result.getSingers()?.get(i)?.setId(id)
        StaticData.Result.getSingers()?.get(i)?.setName(name)
        cover_url?.let { StaticData.Result.getSingers()?.get(i)?.setCover_url(it) }
    }
}