package com.lie.puremusic.pojo

class Lyrics {
    private var content: String? = null
    private var duration : Int = 0
    private var currentPosition: Int = 0

    constructor(content: String?) {
        this.content = content
    }

    fun setContent(content: String?) {
        this.content = content
    }

    fun setCurrentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
    }

    fun setDuration(duration: Int) {
        this.duration = duration
    }
    fun getContent(): String?{
        return this.content
    }
    fun getDuration(): Int{
        return this.duration
    }
    fun getCurrentPosition(): Int{
        return this.currentPosition
    }

}