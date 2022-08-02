package com.lie.puremusic.pojo

class Record {
    private var Style = 0
    private var MusicStyle: String? = null
    private var KeyWord: String? = null

    constructor(Style: Int, KeyWord: String?) {
        this.Style = Style
        this.KeyWord = KeyWord
    }

    fun getStyle(): Int {
        return Style
    }

    fun setStyle(style: Int) {
        Style = style
    }

    fun getKeyWord(): String? {
        return KeyWord
    }

    fun setKeyWord(keyWord: String?) {
        KeyWord = keyWord
    }

    fun getMusicStyle(): String? {
        return MusicStyle
    }

    fun setMusicStyle(musicStyle: String?) {
        MusicStyle = musicStyle
    }

}