package com.lie.puremusic

import com.lie.puremusic.pojo.*
import redis.clients.jedis.Jedis
import java.sql.Connection
import kotlin.collections.ArrayList

class StaticData {
    companion object {
        var debug = false
        var Square_SelectID = 0
        var SelectID = 0
        var Scale = 1f
        var offset = 0
        var KeyWords: String? = null
        var isFirstPlay = false
        var Root = "网易云音乐"
        var Containr: MutableList<String?> = ArrayList()
        var Result: Result = Result()
        var Songs: Song? = null
        var Singer: Singer? = null
        var SongList: SongList? = null
        var Playing_ID = ""
        var Square: MutableList<SquareSongList?> = ArrayList()
        val Home: Home = Home()
        val Cloud: MutableList<Song?> = ArrayList()
        var User: User? = null
        var user: PureUser? = null
        var PlayList_now: MutableList<Song?> = ArrayList()
        var PlayList: MutableList<Song?> = ArrayList()
        var Records: MutableList<Record?> = ArrayList()
        var connection: Connection? = null
        var jedis: Jedis? = null
        var version_date = "2022-8-4"
        var hasNew = false
        val cookie: String =
            "NMTID=00OhbBuqaRou2J1fklzoj2jOrYMIhEAAAGCbP0qxw; MUSIC_U=8de9acde979fb024e459d41dee8a23ea8d1395613e2b05bdea551c67a09705488a08bd5bf851808f73039bd9df1dd379aa714b8311542ff13650ddea49e375e59689f7abbb96f6087a561ba977ae766d; __remember_me=true; __csrf=7c78a442b212d6b492798e89421e9f8f"
    }
}