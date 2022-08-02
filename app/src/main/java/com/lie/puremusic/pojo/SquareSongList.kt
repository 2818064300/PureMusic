package com.lie.puremusic.pojo

import com.lie.puremusic.pojo.SongList

class SquareSongList {
    private var SongsLists: MutableList<SongList?> = ArrayList()

    fun getSongsLists(): MutableList<SongList?>? {
        return SongsLists
    }

    fun setSongsLists(songsLists: ArrayList<SongList?>) {
        SongsLists = songsLists
    }
}