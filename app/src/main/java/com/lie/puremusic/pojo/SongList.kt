package com.lie.puremusic.pojo

class SongList {
    private var id: String? = null
    private var name: String? = null
    private var creator_name: String? = null
    private val Songs: ArrayList<Song?> = ArrayList()
    private var cover_url: String? = null
    private var count = 0
    private var playCount = 0
    private var bookCount = 0
    private var offset = 0

    constructor(id: String?) {
        this.id = id
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getSongs(): java.util.ArrayList<Song?>? {
        return Songs
    }

    fun getCover_url(): String? {
        return cover_url
    }

    fun setCover_url(cover_url: String?) {
        this.cover_url = cover_url
    }

    fun getCount(): Int {
        return count
    }

    fun setCount(count: Int) {
        this.count = count
    }

    fun getOffset(): Int {
        return offset
    }

    fun setOffset(offset: Int) {
        this.offset = offset
    }

    fun getCreator_name(): String? {
        return creator_name
    }

    fun setCreator_name(creator_name: String?) {
        this.creator_name = creator_name
    }

    fun getPlayCount(): Int {
        return playCount
    }

    fun setPlayCount(playCount: Int) {
        this.playCount = playCount
    }

    fun getBookCount(): Int {
        return bookCount
    }

    fun setBookCount(bookCount: Int) {
        this.bookCount = bookCount
    }
}