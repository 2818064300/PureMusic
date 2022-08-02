package com.lie.puremusic.utils

import java.sql.Connection
import java.sql.DriverManager

object DBUtils {
    private var connection: Connection? = null
    fun getConnection(): Connection? {
        Class.forName("com.mysql.jdbc.Driver")
        val url = "jdbc:mysql://rm-bp1445lru7o556z80so.mysql.rds.aliyuncs.com:3306/data"
        val username = "user_0"
        val password = "Lcz123456"
        connection = DriverManager.getConnection(url, username, password)
        return connection
    }

    fun closeConnection() {
        connection?.close()
    }
}