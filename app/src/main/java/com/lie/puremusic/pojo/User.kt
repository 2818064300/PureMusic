package com.lie.puremusic.pojo

class User {
    private var level = 0
    private var listenSongs = 0
    private var id: String? = null
    private var identify: String? = null
    private var Favorite = SongList("2046607227")
    private var SongLists: MutableList<SongList?> = ArrayList()

    constructor(id: String?) {
        this.id = id
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getIdentify(): String? {
        return identify
    }

    fun setIdentify(identify: String?) {
        this.identify = identify
    }

    fun getLevel(): Int {
        return level
    }

    fun setLevel(level: Int) {
        this.level = level
    }

    fun getListenSongs(): Int {
        return listenSongs
    }

    fun setListenSongs(listenSongs: Int) {
        this.listenSongs = listenSongs
    }

    fun getSongLists(): MutableList<SongList?>? {
        return SongLists
    }

    fun setSongLists(songLists: ArrayList<SongList?>) {
        SongLists = songLists
    }

    fun getFavorite(): SongList? {
        return Favorite
    }

    fun setFavorite(favorite: SongList) {
        Favorite = favorite
    }
}