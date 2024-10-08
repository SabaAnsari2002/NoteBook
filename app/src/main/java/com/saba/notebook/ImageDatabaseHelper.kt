package com.saba.notebook

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class ImageDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "images.db"
        private const val DATABASE_VERSION = 6
        const val TABLE_NAME = "Images"
        const val COLUMN_ID = "id"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_HASH = "image_hash"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_IMAGE BLOB NOT NULL," +
                "$COLUMN_HASH TEXT NOT NULL UNIQUE)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // ذخیره تصویر در دیتابیس فقط در صورتی که قبلاً ذخیره نشده باشد
    fun insertImage(image: ByteArray): Long {
        val db = this.writableDatabase

        // محاسبه هش تصویر
        val imageHash = calculateHash(image)

        // بررسی اینکه آیا هش تصویر قبلاً وجود دارد یا خیر
        if (isImageExists(imageHash)) {
            return -1 // تصویر قبلاً وجود داشته است
        }

        // درج تصویر جدید
        val values = ContentValues().apply {
            put(COLUMN_IMAGE, image)
            put(COLUMN_HASH, imageHash)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // تابعی برای بررسی اینکه آیا تصویر با هش مورد نظر وجود دارد یا خیر
    private fun isImageExists(imageHash: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_HASH = ?", arrayOf(imageHash))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // تابعی برای محاسبه هش MD5 از تصویر
    private fun calculateHash(image: ByteArray): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(image)
        return digest.joinToString("") { "%02x".format(it) }
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