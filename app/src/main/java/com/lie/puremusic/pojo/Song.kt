package com.lie.puremusic.pojo

import androidx.palette.graphics.Palette.Swatch

class Song {
    private var id: String? = null
    private var name: String? = null
    private var Singers: MutableList<Singer?> = ArrayList()
    private var cover_url: String? = null
    private var music_url: String? = null
    private var type: String? = null
    private var isMark = false
    private var fee = 0
    private var pop = 0
    private var time = 0
    private var isCould = false
    private var style: String? = null
    private var CurrentPosition = 0
    private var vibrant: Swatch? = null
    private var muted: Swatch? = null
    private var vibrantLight: Swatch? = null
    private var vibrantDark: Swatch? = null
    private var lyric: Lyrics? = null

    constructor()

    constructor(id: String?) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getId(): String? {
        return id
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getCover_url(): String? {
        return cover_url
    }

    fun setCover_url(cover_url: String?) {
        this.cover_url = cover_url
    }

    fun isMark(): Boolean {
        return isMark
    }

    fun setMark(mark: Boolean) {
        isMark = mark
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String?) {
        this.type = type
    }

    fun getMusic_url(): String? {
        return music_url
    }

    fun setMusic_url(music_url: String?) {
        this.music_url = music_url
    }

    fun getLyric(): Lyrics? {
        return lyric
    }

    fun setLyric(lyric: Lyrics?) {
        this.lyric = lyric
    }

    fun getFee(): Int {
        return fee
    }

    fun setFee(fee: Int) {
        this.fee = fee
    }

    fun getPop(): Int {
        return pop
    }

    fun setPop(pop: Int) {
        this.pop = pop
    }

    fun isCould(): Boolean {
        return isCould
    }

    fun setCould(could: Boolean) {
        isCould = could
    }

    fun getTime(): Int {
        return time
    }

    fun setTime(time: Int) {
        this.time = time
    }

    fun getSingers(): MutableList<Singer?>? {
        return Singers
    }

    fun setSingers(singers: java.util.ArrayList<Singer?>) {
        Singers = singers
    }

    fun getStyle(): String? {
        return style
    }

    fun setStyle(style: String?) {
        this.style = style
    }

    fun getCurrentPosition(): Int {
        return CurrentPosition
    }

    fun setCurrentPosition(currentPosition: Int) {
        CurrentPosition = currentPosition
    }
    fun getVibrant(): Swatch? {
        return vibrant
    }

    fun setVibrant(vibrant: Swatch?) {
        this.vibrant = vibrant
    }

    fun getVibrantLight(): Swatch? {
        return vibrantLight
    }

    fun setVibrantLight(vibrantLight: Swatch?) {
        this.vibrantLight = vibrantLight
    }

    fun getVibrantDark(): Swatch? {
        return vibrantDark
    }

    fun setVibrantDark(vibrantDark: Swatch?) {
        this.vibrantDark = vibrantDark
    }

    fun getMuted(): Swatch? {
        return muted
    }

    fun setMuted(muted: Swatch?) {
        this.muted = muted
    }
}