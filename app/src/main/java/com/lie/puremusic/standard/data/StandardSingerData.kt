package com.lie.puremusic.standard.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 标准歌单数据类
 * @author Moriafly
 */
@Parcelize
data class StandardSingerData(
    // 歌手id
    var id: Long,
    // 歌手名字
    var name: String,
    // 歌手图片
    var picUrl: String,
    // 内含歌曲
    var songs: ArrayList<StandardSongData>
): Parcelable