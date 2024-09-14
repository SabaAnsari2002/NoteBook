package com.saba.notebook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ImageDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "images.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Images"
        const val COLUMN_ID = "id"
        const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_IMAGE BLOB NOT NULL)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // ذخیره تصویر در دیتابیس
    fun insertImage(image: ByteArray): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_IMAGE, image)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // خواندن تمام تصاویر از دیتابیس
    fun getAllImages(): List<ByteArray> {
        val imageList = mutableListOf<ByteArray>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor.moveToFirst()) {
            do {
                val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
                imageList.add(image)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return imageList
    }
}
