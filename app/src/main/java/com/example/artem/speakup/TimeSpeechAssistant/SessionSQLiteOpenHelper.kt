package com.example.artem.speakup.TimeSpeechAssistant

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.service.textservice.SpellCheckerService

/**
 * Created by ASUS on 01.03.2018.
 */

class SessionSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(SessionDBContract.Sessions.CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SessionDBContract.Sessions.TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    companion object {
        private val DATABASE_VERSION = 1
        val DATABASE_NAME = "session_database"
    }
}