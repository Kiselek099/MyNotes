package com.example.mynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "notes_database"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "notes_table"
        const val KEY_ID = "id"
        const val KEY_TEXT = "text"
        const val KEY_COMPLETED = "completed"
        const val KEY_CREATED_AT = "created_at"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE " + TABLE_NAME + " (" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TEXT + " TEXT, " +
                KEY_COMPLETED + " INTEGER, " +
                KEY_CREATED_AT + " TEXT)")
        db?.execSQL(createTableQuery)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
    fun addNote(text: String, completed: Boolean, createdAt: String) {
        val values = ContentValues().apply {
            put(KEY_TEXT, text)
            put(KEY_COMPLETED, if (completed) 1 else 0)
            put(KEY_CREATED_AT, createdAt)
        }
        val db = writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getNotes(): Cursor? {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    fun removeAllNotes() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
    fun updateNote(id: Int, newText: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(KEY_TEXT, newText)
        }

        val whereClause = "$KEY_ID = ?"
        val whereArgs = arrayOf(id.toString())
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
        db.close()
    }
}