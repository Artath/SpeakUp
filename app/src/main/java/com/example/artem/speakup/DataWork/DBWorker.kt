package com.example.artem.speakup.DataWork

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.artem.speakup.TimeSpeechAssistant.Data.AsissrantSQLiteOpenHelper
import kotlin.collections.ArrayList

// base class for data base work
abstract class DBWorker (var context: Context) : ExtraSourceWorker {

    protected open val values = ContentValues()

    protected open val projection: Array<String>? = null
    protected open val tableName: String? = null
    protected open var selection: String? = null
    protected open var selectionArgs: Array<String>? = null

    override fun read(): ArrayList<out ExtraSourceObject> =
            cursorToArrayList(AsissrantSQLiteOpenHelper(context).readableDatabase.query(
                    tableName,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null))

    override fun create(): Long =
            AsissrantSQLiteOpenHelper(context).writableDatabase.insert(tableName, null, values)

    override fun update(): Int = AsissrantSQLiteOpenHelper(context).readableDatabase.update(
            tableName,
            values,
            selection,
            selectionArgs)

    override fun delete(): Int = AsissrantSQLiteOpenHelper(context).readableDatabase.delete(
            tableName,
            selection,
            selectionArgs)

    open fun addSelection(selection: String) {
        this.selection = selection
    }

    open fun addSelectionArgs(selectionArgs: Array<String>) {
        this.selectionArgs = selectionArgs
    }

    //this method return need array list of need object. it implements child-klasses
    abstract fun cursorToArrayList(cursor: Cursor) : ArrayList<out ExtraSourceObject>

}