package com.lie.puremusic.pojo

class PureUser {
    private var id: String? = null
    private var account: String? = null
    private var password: String? = null
    private var music_id: String? = null
    private var name: String? = null
    private var avatarUrl: String? = null

    constructor(id: String?, account: String?, password: String?) {
        this.id = id
        this.account = account
        this.password = password
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String?) {
        this.id = id
    }

    fun getAccount(): String? {
        return account
    }

    fun setAccount(account: String?) {
        this.account = account
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    fun getMusic_id(): String? {
        return music_id
    }

    fun setMusic_id(music_id: String?) {
        this.music_id = music_id
    }

    override fun toString(): String {
        return """
             ID: $id
             账号: $account
             密码: $password
             
             """.trimIndent()
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getAvatarUrl(): String? {
        return avatarUrl
    }

    fun setAvatarUrl(avatarUrl: String?) {
        this.avatarUrl = avatarUrl
    }
}