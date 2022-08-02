package com.lie.puremusic

import com.lie.puremusic.pojo.*
import java.sql.Connection
import kotlin.collections.ArrayList

class StaticData {
    companion object {
        var debug = false;
        var Style: String? = null
        var SongsList_ID = 0
        var Singer_ID = 0
        var Singer_Result_ID = 0
        var SongList_Result_ID = 0
        var SongList_Music_ID = 0
        var Square_SelectID = 0
        var Square_Position = 0
        var SelectID = 0
        var Scale = 1f
        var offset = 0
        var KeyWords: String? = null
        var isFirstPlay = false
        var Root = "网易云音乐"
        var Containr: MutableList<String?> = ArrayList()
        var Result: Result = Result()
        var hitokoto = Hitokoto()
        var Songs: Song? = null
        var Singer: Singer? = null
        var SongList: SongList? = null
        var Playing_ID = ""
        var Square: MutableList<SquareSongList?> = ArrayList()
        val Home: Home = Home()
        val Cloud : MutableList<Song?> = ArrayList()
        var User: User? = null
        var user: PureUser? = null
        var PlayList_now: MutableList<Song?> = ArrayList()
        var PlayList: MutableList<Song?> = ArrayList()
        var Records: MutableList<Record?> = ArrayList()
        var connection: Connection? = null
        var version_date = "2022-7-25"
        var hasNew = false
        val cookie: String =
            "NMTID=00O6vdzXlrHqzdpTUJ6skNMFds2-2QAAAF_Z5_P_Q; MUSIC_U=8de9acde979fb024e459d41dee8a23ea8182fe18fe02ee0f9c8d2ea8fee9e3978a08bd5bf851808fddfbc02b025cf2780359427093f3f6753650ddea49e375e59689f7abbb96f6087a561ba977ae766d; __remember_me=true; __csrf=ba52b4aef3fa67216b39f2f160f0237f"
    }
}