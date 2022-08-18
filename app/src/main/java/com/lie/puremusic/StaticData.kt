package com.lie.puremusic

import com.lie.puremusic.data.LyricViewData
import com.lie.puremusic.music.netease.*
import com.lie.puremusic.music.netease.data.UserDetailData
import com.lie.puremusic.music.netease.data.UserPlaylistData
import com.lie.puremusic.pojo.*
import com.lie.puremusic.standard.data.StandardPlaylistData
import com.lie.puremusic.standard.data.StandardSingerData
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.standard.data.StandardSongDataEx
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
        var Singer: Singer? = null
        var SongList: SongList? = null
        var Playing_ID = ""
        var Square: MutableList<SquareSongList?> = ArrayList()
        var PlaylistRecommend : ArrayList<PlaylistRecommend.PlaylistRecommendDataResult>? = null
        var SingerRecommend : ArrayList<SingerRecommend.SingerRecommendDataResult>? = null
        var UserDetailData : UserDetailData? = null
        var MyFavorite : UserPlaylistData.Playlist? = null
        var UserPlaylistData : ArrayList<UserPlaylistData.Playlist>? = null
        var user: PureUser? = null
        var PlayList: MutableList<Song?> = ArrayList()
        var Records: MutableList<Record?> = ArrayList()
        var connection: Connection? = null
        var jedis: Jedis? = null
        var version_date = "2022-8-4"
        var hasNew = false
        val Cloud: ArrayList<StandardSongData> = ArrayList()
        var SearchResult: ArrayList<StandardSongData> = ArrayList()
        var NewSong: ArrayList<StandardSongData> = ArrayList()
        var CurrentPosition: Long? = null
        var Songs: StandardSongData? = null
        var PlayListData: StandardPlaylistData? = null
        var SingerData: StandardSingerData? = null
        var PlayList_now: StandardPlaylistData? = null
        var PlayDataEx: StandardSongDataEx? = null
        var SongUrl: String? = null
        var SongLrc: LyricViewData? = null
        var Position = 0
        var isCloud : Boolean = false
        val cookie: String =
            "NMTID=00OABGkx53WhxqYPkwKokFu94D5p3QAAAGChdig5Q; Max-Age=315360000; Expires=Sat, 07 Aug 2032 03:42:22 GMT; Path=/;;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_U=8de9acde979fb024e459d41dee8a23ea5d7c2b1b5619dad8e72d1493644457008a08bd5bf851808fe21b68546e8b351ccade798774a697593650ddea49e375e59689f7abbb96f608d4dbf082a8813684; Max-Age=15552000; Expires=Mon, 06 Feb 2023 03:42:22 GMT; Path=/; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/feedback; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/openapi/clientlog; HTTPOnly;__csrf=31f6c5c5f7f0453332eef862ccf5f67c; Max-Age=1296010; Expires=Thu, 25 Aug 2022 03:42:32 GMT; Path=/;;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_SNS=; Max-Age=0; Expires=Wed, 10 Aug 2022 03:42:22 GMT; Path=/;MUSIC_R_T=1515216803144; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/openapi/clientlog; HTTPOnly;MUSIC_A_T=1515216786571; Max-Age=2147483647; Expires=Mon, 28 Aug 2090 06:56:29 GMT; Path=/api/clientlog; HTTPOnly"
    }
}