package com.lie.puremusic.pojo

class Singer {
    private var id: String? = null
    private var name: String? = null
    private var Songs: MutableList<Song?> = ArrayList()
    private var cover_url: String? = null
    private var isMark = false

    constructor()
    constructor(id: String?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getSongs(): MutableList<Song?>? {
        return Songs
    }

    fun getCover_url(): String? {
        return cover_url
    }

    fun setCover_url(cover_url: String) {
        this.cover_url = cover_url
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun isMark(): Boolean {
        return isMark
    }

    fun setMark(mark: Boolean) {
        isMark = mark
    }
}