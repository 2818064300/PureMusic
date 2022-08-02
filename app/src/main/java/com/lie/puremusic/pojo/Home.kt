package com.lie.puremusic.pojo

class Home {
    private var SongsLists: MutableList<SongList?> = ArrayList()
    private var Singers: MutableList<Singer?> = ArrayList()
    fun getSongsLists(): MutableList<SongList?>? {
        return SongsLists
    }

    fun setSongsLists(songsLists: MutableList<SongList?>) {
        SongsLists = songsLists
    }

    fun getSingers(): MutableList<Singer?>? {
        return Singers
    }

    fun setSingers(singers: MutableList<Singer?>) {
        Singers = singers
    }
}