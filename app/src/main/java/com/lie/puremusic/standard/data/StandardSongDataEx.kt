package com.lie.puremusic.standard.data

import androidx.palette.graphics.Palette

data class StandardSongDataEx(
    // 歌曲ID
    var id: String?,
    // 颜色
    var Vibrant: Palette.Swatch?,
    var VibrantLight: Palette.Swatch?,
    var VibrantDark: Palette.Swatch?,
    var Muted: Palette.Swatch?
)