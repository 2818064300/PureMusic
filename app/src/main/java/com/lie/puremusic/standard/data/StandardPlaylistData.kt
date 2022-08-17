package com.lie.puremusic.standard.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 标准歌单数据类
 * @author Moriafly
 */
@Parcelize
data class StandardPlaylistData(
    // 歌单id
    var id: Long,
    // 歌单名字
    var name: String,
    // 歌单图片
    var picUrl: String,
    // 歌单播放量
    var playCount: Long,
    // 内含歌曲
    var songs: ArrayList<StandardSongData>
): Parcelable