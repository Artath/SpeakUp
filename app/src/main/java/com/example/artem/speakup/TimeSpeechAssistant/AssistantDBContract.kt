package com.example.artem.speakup.TimeSpeechAssistant

import android.provider.BaseColumns

class AssistantDBContract {

    class Sessions : BaseColumns {
        companion object {
            val TABLE_NAME = "sessions"
            val COLUMN_NAME = "sessionsName"
            val COLUMN_DESCRIPTION = "description"
            val COLUMN_CREATED_DATE = "createdDate"
            val COLUMN_RAITING = "raiting"
            val _ID = BaseColumns._ID

            val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_CREATED_DATE + " INTEGER, " +
                    COLUMN_RAITING + " INTEGER" + ")"

        }
    }

    class Parts : BaseColumns {
        companion object {
            val TABLE_NAME = "parts"
            val COLUMN_HEAD = "head"
            val COLUMN_THESES = "theses"
            val COLUMN_TIME = "time"
            val COLUMNE_ORDER = "order_move"
            val SESSION_ID = "session_id"
            val _ID = BaseColumns._ID

            val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SESSION_ID + " INTEGER, " +
                    COLUMN_HEAD + " TEXT, " +
                    COLUMN_THESES + " TEXT, " +
                    COLUMN_TIME + " INTEGER, " +
                    COLUMNE_ORDER + " INTEGER, " +
                    "FOREIGN KEY ("+SESSION_ID+") REFERENCES "+Sessions.TABLE_NAME+"("+ Sessions._ID+"))"
        }
    }

}