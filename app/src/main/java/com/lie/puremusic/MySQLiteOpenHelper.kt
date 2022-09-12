package com.lie.puremusic

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteOpenHelper(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    var create = "create table user (" +
            "id integer primary key autoincrement," +
            "name text)"


    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(create)
        println("执行成功")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
//        TODO("Not yet implemented")
    }

    fun insert() {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", "张三")
        db.insert("user", null, values)
    }

    fun update() {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", "张三")
        db.update("user",values,null,null)
    }

    fun detele(){
        val db = writableDatabase
        db.delete("user", null,null)
        println("删除成功")
    }

    fun query() {
        val db = writableDatabase
        val cursor = db.query("user", null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                println(id)
                println(name)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}