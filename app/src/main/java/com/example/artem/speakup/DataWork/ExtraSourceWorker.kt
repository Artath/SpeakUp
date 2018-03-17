package com.example.artem.speakup.DataWork

//interface for CRUD request from extra source
interface ExtraSourceWorker {

    fun read(): ArrayList<out ExtraSourceObject>
    fun create(): Long
    fun update(): Int
    fun delete(): Int

}