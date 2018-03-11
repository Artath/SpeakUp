package com.example.artem.speakup.DataWork

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.artem.speakup.TimeSpeechAssistant.AsissrantSQLiteOpenHelper
import kotlin.collections.ArrayList

// base class for data base work
abstract class DBWorker : ExtraSourceWorker {

    protected open val values = ContentValues()

    protected open val projection: Array<String>? = null
    protected open val tableName: String? = null
    protected open var selection: String? = null
    protected open var selectionArgs: Array<String>? = null

    override fun read(context: Context): ArrayList<out ExtraSourceObject> {

        val DB = AsissrantSQLiteOpenHelper(context).readableDatabase
        return cursorToArrayList(DB.query(
                tableName,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null))
    }

    override fun create(context: Context): Long {
        val DB = AsissrantSQLiteOpenHelper(context).writableDatabase
        return DB.insert(tableName, null, values)
    }

    override fun update(context: Context): Int {
        val DB = AsissrantSQLiteOpenHelper(context).readableDatabase
        return DB.update(
                tableName,
                values,
                selection,
                selectionArgs)
    }

    override fun delete(context: Context): Int {
        val DB = AsissrantSQLiteOpenHelper(context).readableDatabase
        return DB.delete(
                tableName,
                selection,
                selectionArgs)
    }

    open fun addSelection(selection: String) {
        this.selection = selection
    }

    open fun addSelectionArgs(selectionArgs: Array<String>) {
        this.selectionArgs = selectionArgs
    }

    //this method return need array list of need object. it implements child-klasses
    abstract fun cursorToArrayList(cursor: Cursor) : ArrayList<out ExtraSourceObject>

}