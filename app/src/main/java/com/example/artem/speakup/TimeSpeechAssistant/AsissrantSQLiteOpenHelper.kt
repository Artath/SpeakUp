package com.example.artem.speakup.TimeSpeechAssistant

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by ASUS on 01.03.2018.
 */

class AsissrantSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(AssistantDBContract.Sessions.CREATE_TABLE)
        sqLiteDatabase.execSQL(AssistantDBContract.Parts.CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AssistantDBContract.Sessions.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AssistantDBContract.Parts.TABLE_NAME)
        onCreate(sqLiteDatabase)
    }

    companion object {
        private val DATABASE_VERSION = 1
        val DATABASE_NAME = "SessionDatabase"
    }
}