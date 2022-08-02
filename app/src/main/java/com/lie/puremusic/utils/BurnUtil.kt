package com.lie.puremusic.utils

import android.graphics.Color

class BurnUtil {
    companion object {
        fun colorBurn(RGBValues: Int): Int {
            val alpha = RGBValues shr 24
            var red = RGBValues shr 16 and 0xFF
            var green = RGBValues shr 8 and 0xFF
            var blue = RGBValues and 0xFF
            red = Math.floor(red * (1 - 0.2)).toInt()
            green = Math.floor(green * (1 - 0.2)).toInt()
            blue = Math.floor(blue * (1 - 0.2)).toInt()
            return Color.rgb(red, green, blue)
        }
    }
}