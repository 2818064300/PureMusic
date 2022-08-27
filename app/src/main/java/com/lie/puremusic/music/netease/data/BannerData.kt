package com.lie.puremusic.music.netease.data

import androidx.annotation.Keep

@Keep
data class BannerData(
    val banners: ArrayList<Banner>,
    val code: Int
) {
    data class Banner(
        val typeTitle: String,
        val url: String,
        val imageUrl: String
    )
}
