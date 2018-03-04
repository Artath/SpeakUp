package com.example.artem.speakup.TimeSpeechAssistant

import android.provider.BaseColumns

/**
 * Created by ASUS on 01.03.2018.
 */
class SessionDBContract {

    class Sessions : BaseColumns {
        companion object {
            val TABLE_NAME = "sessions"
            val COLUMN_NAME = "sessionsName"
            val COLUMN_DESCRIPTION = "description"
            val COLUMN_CREATED_DATE = "createdDate"
            val COLUMN_PARTS = "pasrts"
            val COLUMN_RAITING = "raiting"
            val _ID = BaseColumns._ID

            val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    TABLE_NAME + " (" +
                    BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_CREATED_DATE + " INTEGER, " +
                    COLUMN_PARTS + " Text," +
                    COLUMN_RAITING + " INTEGER" + ")"

        }
    }

}