package com.lie.puremusic.pojo

class Result {
    private val Songs: MutableList<Song?> = ArrayList()
    private val Singers: MutableList<Singer?> = ArrayList()
    private var SongLists: MutableList<SongList?> = ArrayList()

    fun getSongs(): MutableList<Song?>? {
        return Songs
    }

    fun getSingers(): MutableList<Singer?>? {
        return Singers
    }

    fun getSongLists(): MutableList<SongList?>? {
        return SongLists
    }

    fun setSongLists(songLists: ArrayList<SongList?>) {
        SongLists = songLists
    }
}