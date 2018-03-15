package com.example.artem.speakup.TimeSpeechAssistant.Data

import android.content.Context
import android.database.Cursor
import com.example.artem.speakup.DataWork.DBWorker

class DBWorkParts(context: Context) : DBWorker(context) {

    override val projection = arrayOf<String>(AssistantDBContract.Parts._ID,
            AssistantDBContract.Parts.COLUMN_HEAD,
            AssistantDBContract.Parts.COLUMN_THESES,
            AssistantDBContract.Parts.COLUMN_TIME,
            AssistantDBContract.Parts.COLUMNE_ORDER,
            AssistantDBContract.Parts.SESSION_ID)

    override val tableName = AssistantDBContract.Parts.TABLE_NAME

    fun setHead(head: String) = values.put(AssistantDBContract.Parts.COLUMN_HEAD, head)

    fun setTheses(theses: String) = values.put(AssistantDBContract.Parts.COLUMN_THESES, theses)

    fun setTime(time: Long) = values.put(AssistantDBContract.Parts.COLUMN_TIME, time)

    fun setSessionId(id: Long) =  values.put(AssistantDBContract.Parts.SESSION_ID, id)

    fun setOrder(order: Int) = values.put(AssistantDBContract.Parts.COLUMNE_ORDER, order)

    //except sessionId
    fun setAllValues(part: Part) {
        setHead(part.head)
        setTheses(part.theses)
        setTime(part.time)
    }

    //concrate realization
    override fun cursorToArrayList(cursor: Cursor): ArrayList<Part>  {
        val arrayList = arrayListOf<Part>()
        while (cursor.moveToNext()) {
            arrayList.add(Part(cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Parts._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AssistantDBContract.Parts.COLUMN_HEAD)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Parts.COLUMN_TIME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AssistantDBContract.Parts.COLUMN_THESES)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(AssistantDBContract.Parts.SESSION_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(AssistantDBContract.Parts.COLUMNE_ORDER))))
        }

        return arrayList
    }
}