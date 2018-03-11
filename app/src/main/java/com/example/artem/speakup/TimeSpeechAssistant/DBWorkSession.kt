package com.example.artem.speakup.TimeSpeechAssistant

import android.database.Cursor
import com.example.artem.speakup.DataWork.DBWorker
import java.util.*

class DBWorkSession : DBWorker() {

    override val projection = arrayOf<String>(AssistantDBContract.Sessions._ID,
            AssistantDBContract.Sessions.COLUMN_NAME,
            AssistantDBContract.Sessions.COLUMN_DESCRIPTION,
            AssistantDBContract.Sessions.COLUMN_CREATED_DATE,
            AssistantDBContract.Sessions.COLUMN_RAITING)

    override val tableName = AssistantDBContract.Sessions.TABLE_NAME

    fun setName(name: String) = values.put(AssistantDBContract.Sessions.COLUMN_NAME, name)

    fun setDescription(descr: String) =   values.put(AssistantDBContract.Sessions.COLUMN_DESCRIPTION, descr)

    fun setRaiting(raiting: Int) =  values.put(AssistantDBContract.Sessions.COLUMN_RAITING, raiting)

    fun setCurrentDate() = values.put(AssistantDBContract.Sessions.COLUMN_CREATED_DATE, Date().time)

    //concrate realization
   override fun cursorToArrayList(cursor: Cursor): ArrayList<SpeechSession>  {

       val arrayList = arrayListOf<SpeechSession>()
        while (cursor.moveToNext()) {
            arrayList.add(SpeechSession(cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_CREATED_DATE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_RAITING))))
        }
        return arrayList
    }


}