package com.lie.puremusic.pojo

class Cloud {
    private var id: Int = 0
    private var name: String? = null
    private var singer_name: String? = null
    private var type: String? = null

    constructor(id: Int, name: String?, singer_name: String?,type : String?) {
        this.id = id
        this.name = name
        this.singer_name = singer_name
        this.type = type
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getId(): Int {
        return id
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getName(): String? {
        return name
    }

    fun setSingerName(singer_name: String?) {
        this.singer_name = singer_name
    }

    fun getSingerName(): String? {
        return singer_name
    }

    fun setType(singer_name: String?) {
        this.type = type
    }

    fun getType(): String? {
        return type
    }
}