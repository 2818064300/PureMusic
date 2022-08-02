package com.lie.puremusic.utils

import com.lie.puremusic.pojo.Cloud
import com.lie.puremusic.pojo.PureUser
import com.lie.puremusic.utils.DBUtils.closeConnection
import java.sql.Connection
import java.sql.SQLException

object Dao {
    fun select(connection: Connection?, user: PureUser): PureUser? {
        try {
            val sql = "select * from user_information where account = ? and password = ?"
            val preparedStatement = connection?.prepareStatement(sql)
            preparedStatement?.setString(1, user.getAccount())
            preparedStatement?.setString(2, user.getPassword())
            val resultSet = preparedStatement?.executeQuery()
            if (resultSet != null) {
                return if (resultSet.next()) {
                    PureUser(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3))
                } else {
                    PureUser("null", "null", "null")
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeConnection()
        }
        return null
    }

    fun insert(connection: Connection?, user: PureUser) {
        try {
            val sql = "insert into user_information(id,account,password) values(?,?,?)"
            val preparedStatement = connection?.prepareStatement(sql)
            preparedStatement?.setString(1, user.getId())
            preparedStatement?.setString(2, user.getAccount())
            preparedStatement?.setString(3, user.getPassword())
            preparedStatement?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeConnection()
        }
    }

    fun check_version(connection: Connection?): String? {
        try {
            val sql = "select * from version"
            val preparedStatement = connection?.prepareStatement(sql)
            val resultSet = preparedStatement?.executeQuery()
            if (resultSet != null) {
                return if (resultSet.next()) {
                    resultSet.getString(2)
                } else {
                    "null"
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeConnection()
        }
        return null
    }

    fun getCloud_info(connection: Connection?, id : String): Cloud? {
        try {
            val sql = "select * from cloud_music where id = ?"
            val preparedStatement = connection?.prepareStatement(sql)
            preparedStatement?.setString(1, id)
            val resultSet = preparedStatement?.executeQuery()
            println(resultSet)
            if (resultSet != null) {
                return if (resultSet.next()) {
                    println(resultSet.getInt(1))
                    println(resultSet.getString(1))
                    Cloud(resultSet.getInt(1), resultSet.getString(1), resultSet.getString(2),resultSet.getString(3))
                } else {
                    Cloud(-1,"null","null","null")
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            closeConnection()
        }
        return null
    }
}