package com.example.artem.speakup.TimeSpeechAssistant.Data

import android.content.Context
import android.database.Cursor
import com.example.artem.speakup.DataWork.DBWorker
import java.util.*

class DBWorkSession(context: Context) : DBWorker(context) {

    override val projection = arrayOf<String>(AssistantDBContract.Sessions._ID,
            AssistantDBContract.Sessions.COLUMN_NAME,
            AssistantDBContract.Sessions.COLUMN_DESCRIPTION,
            AssistantDBContract.Sessions.COLUMN_DURATION,
            AssistantDBContract.Sessions.COLUMN_NUMB_PARTS)

    override val tableName = AssistantDBContract.Sessions.TABLE_NAME

    fun setName(name: String) = values.put(AssistantDBContract.Sessions.COLUMN_NAME, name)

    fun setDescription(descr: String) =   values.put(AssistantDBContract.Sessions.COLUMN_DESCRIPTION, descr)

    fun setDuration(duration: Long) =  values.put(AssistantDBContract.Sessions.COLUMN_DURATION, duration)

    fun setPartsNumb(partNumb: Int) = values.put(AssistantDBContract.Sessions.COLUMN_NUMB_PARTS, partNumb)

    fun setAllValues(session: SpeechSession) {
        setName(session.name)
        setDescription(session.description)
        setDuration(session.duration)
        setPartsNumb(session.numbParts)
    }

    //concrate realization
   override fun cursorToArrayList(cursor: Cursor): ArrayList<SpeechSession>  {

       val arrayList = arrayListOf<SpeechSession>()
        while (cursor.moveToNext()) {
            arrayList.add(SpeechSession(cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_DESCRIPTION)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_DURATION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(AssistantDBContract.Sessions.COLUMN_NUMB_PARTS)), false))
        }
        return arrayList
    }


}