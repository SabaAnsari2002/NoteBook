package com.saba.notebook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserDB"
        private const val TABLE_USERS = "users"
        private const val KEY_ID = "id"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USERS_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_USERNAME + " TEXT UNIQUE,"  // Add UNIQUE constraint
                + KEY_PASSWORD + " TEXT" + ")")
        db.execSQL(CREATE_USERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
    fun addUser(username: String, password: String): Long {
        if (isUsernameExists(username)) {
            return -1 // Return -1 if username already exists
        }

        val db = this.writableDatabase
        val values = ContentValues()
        values.put(KEY_USERNAME, username)
        values.put(KEY_PASSWORD, password)
        return db.insert(TABLE_USERS, null, values)
    }

    fun isUsernameExists(username: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID), "$KEY_USERNAME=?",
            arrayOf(username), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(KEY_ID), "$KEY_USERNAME=? AND $KEY_PASSWORD=?",
            arrayOf(username, password), null, null, null)
        val result = cursor.count > 0
        cursor.close()
        return result
    }
}