package com.lie.puremusic.pojo

class Hitokoto {
    private var hitokoto: String? = null
    private var from: String? = null
    private var from_who: String? = null

    fun getHitokoto(): String? {
        return hitokoto
    }

    fun getFrom(): String? {
        return from
    }

    fun getFrom_who(): String? {
        return from_who
    }

    fun setHitokoto(hitokoto: String?) {
        this.hitokoto = hitokoto
    }

    fun setFrom(from: String?) {
        this.from = from
    }

    fun setFrom_who(from_who: String?) {
        this.from_who = from_who
    }

}