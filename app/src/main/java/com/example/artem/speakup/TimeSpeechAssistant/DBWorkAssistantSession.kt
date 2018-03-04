package com.example.artem.speakup.TimeSpeechAssistant

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

//class from DBSession queries
class DBWorkAssistantSession {

    //need for convert data base's string to ArrayList
    private  var type = object : TypeToken<ArrayList<Part>>() {}.type

    //get all SpeechSessios or one by id
    fun getSessions(context: Context, id: Long?): ArrayList<SpeechSession> {

        var selection: String? = null
        var selectionArgs: Array<String>? = null

        val DB = SessionSQLiteOpenHelper(context).readableDatabase
        val projection = arrayOf<String>(SessionDBContract.Sessions._ID,
                SessionDBContract.Sessions.COLUMN_NAME,
                SessionDBContract.Sessions.COLUMN_DESCRIPTION,
                SessionDBContract.Sessions.COLUMN_CREATED_DATE,
                SessionDBContract.Sessions.COLUMN_PARTS,
                SessionDBContract.Sessions.COLUMN_RAITING)

        if (id != null) {
             selection = SessionDBContract.Sessions._ID + " LIKE ?"
             selectionArgs = arrayOf<String>("" + id)
        }

        return cursorToArrayList(DB.query(
                SessionDBContract.Sessions.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null))

    }
    //save SpeechSession
    fun saveToDBase(context: Context, name: String, description: String, parts: ArrayList<Part>): Long {

        val values = ContentValues()

        values.put(SessionDBContract.Sessions.COLUMN_NAME, name)
        values.put(SessionDBContract.Sessions.COLUMN_DESCRIPTION, description)
        values.put(SessionDBContract.Sessions.COLUMN_PARTS, Gson().toJson(parts))
        values.put(SessionDBContract.Sessions.COLUMN_RAITING, 0)

        values.put(SessionDBContract.Sessions.COLUMN_CREATED_DATE, Date().time)
        val DB = SessionSQLiteOpenHelper(context).writableDatabase
        return DB.insert(SessionDBContract.Sessions.TABLE_NAME, null, values)

    }

    fun deleteSession(context: Context, id: Long) {

        val DB = SessionSQLiteOpenHelper(context).readableDatabase
        val selection = SessionDBContract.Sessions._ID + " LIKE ?"
        val selectionArgs = arrayOf<String>("" + id)
        DB.delete(
                SessionDBContract.Sessions.TABLE_NAME,
                selection,
                selectionArgs)

    }

    fun updateSession(context: Context, id: Long, session: SpeechSession) {

        val values = ContentValues()

        values.put(SessionDBContract.Sessions.COLUMN_NAME, session.name)
        values.put(SessionDBContract.Sessions.COLUMN_DESCRIPTION, session.description)
        values.put(SessionDBContract.Sessions.COLUMN_PARTS, Gson().toJson(session.parts))
        values.put(SessionDBContract.Sessions.COLUMN_RAITING, session.lastRaiting)

        val taskDB = SessionSQLiteOpenHelper(context).readableDatabase

        val selection = SessionDBContract.Sessions._ID + " LIKE ?"
        val selectionArgs = arrayOf<String>("" + id)

        taskDB.update(
                SessionDBContract.Sessions.TABLE_NAME,
                values,
                selection,
                selectionArgs)

    }
    //method for convert table's cursor to ArrayList
    private fun cursorToArrayList(cursor: Cursor): ArrayList<SpeechSession>  {
        val arrayList = arrayListOf<SpeechSession>()
        while (cursor.moveToNext()) {
            arrayList.add(SpeechSession(cursor.getLong(cursor.getColumnIndexOrThrow(SessionDBContract.Sessions._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SessionDBContract.Sessions.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SessionDBContract.Sessions.COLUMN_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(SessionDBContract.Sessions.COLUMN_CREATED_DATE)),
                    Gson().fromJson(cursor.getString(cursor.getColumnIndexOrThrow(SessionDBContract.Sessions.COLUMN_PARTS)), type),
                    cursor.getInt(cursor.getColumnIndexOrThrow(SessionDBContract.Sessions.COLUMN_RAITING))))

        }
        return arrayList
    }
}