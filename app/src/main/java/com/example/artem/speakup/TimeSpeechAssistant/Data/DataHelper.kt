package com.example.artem.speakup.TimeSpeechAssistant.Data
import com.example.artem.speakup.DataWork.ExtraSourceWorker

class DataHelper {

    companion object {

        fun readSession(extrW: ExtraSourceWorker, id: Long?): ArrayList<SpeechSession> =
            when (extrW) {
                is DBWorkSession -> {
                    readSessionFromLocalDB(extrW, id)
                }
                else -> {
                   arrayListOf<SpeechSession>()
                }
            }


        fun readPart(extrW: ExtraSourceWorker, sessionId: Long): ArrayList<Part> =
            when (extrW) {
                is DBWorkParts -> {
                    readPartsFromLocalDB(extrW, sessionId)
                }
                else -> {
                    arrayListOf<Part>()
                }
            }


        fun deleteSessionWithParts(extrW: ExtraSourceWorker, id: Long) {
            when (extrW) {
                is DBWorkSession -> {
                    deleteSessionFromLocalDB(extrW, id)
                    deletePartsFromLocalDB(DBWorkParts(extrW.context), id, true)
                }
            }
        }

        fun deletePart(extrW: ExtraSourceWorker, id: Long) {
            when (extrW) {
                is DBWorkParts -> {
                    deletePartsFromLocalDB(extrW, id, false)
                }
            }
        }

        fun createSession(extrW: ExtraSourceWorker, session: SpeechSession): Long =
            when (extrW) {
                is DBWorkSession -> {
                    saveSessionToLocalDB(extrW, session)
                }

                else -> -1
            }

        fun createSeveralPart(extrW: ExtraSourceWorker, sessionId: Long, parts: ArrayList<Part>) {
            when (extrW) {
                is DBWorkParts -> {
                    savePartsToLocalBD(extrW, sessionId, parts)
                }
            }
        }

        fun updateSession(extrW: ExtraSourceWorker, id: Long, session: SpeechSession) {
            when (extrW) {
                is DBWorkSession -> {
                    updateSessionFromLocalBD(extrW, id, session)
                }
            }
        }

        fun customUpdatePart(extrW: ExtraSourceWorker, sessionId: Long, parts: ArrayList<Part>) {
            when (extrW) {
                is DBWorkParts -> {
                    updateOrCreatePartsFromLocalBD(extrW,sessionId, parts)
                }
            }
        }
        fun prepareSpeech(speechName: String, parts: ArrayList<Part>): SpeechSession {
            var descr = ""
            val duration = parts.map { it.time }.sum()
            for (elem in parts) {
                descr += elem.head + " "
                if (descr.length > 30) {
                    descr = descr.substring(0, 30) + "..."
                    break
                }
            }
            return SpeechSession(-1, speechName, descr, duration, parts.size, false)
        }

        private fun readSessionFromLocalDB(dbw: DBWorkSession, id: Long?): ArrayList<SpeechSession> {
            if (id != null) {
                dbw.addSelection(AssistantDBContract.Sessions._ID + " LIKE ?")
                dbw.addSelectionArgs(arrayOf<String>(id.toString()))
            }
            return  dbw.read() as ArrayList<SpeechSession>
        }

        private fun readPartsFromLocalDB(dbw: DBWorkParts, id: Long): ArrayList<Part> {
            dbw.addSelection(AssistantDBContract.Parts.SESSION_ID + " LIKE ?")
            dbw.addSelectionArgs(arrayOf<String>(id.toString()))
            return  dbw.read() as ArrayList<Part>
        }

        private fun saveSessionToLocalDB(dbw: DBWorkSession, session: SpeechSession): Long {
            dbw.setAllValues(session)
            return dbw.create()
        }

        private fun savePartsToLocalBD(dbw: DBWorkParts, sessionId: Long, parts: ArrayList<Part>) {
            dbw.setSessionId(sessionId)
            parts.forEach { elem ->
                dbw.setAllValues(elem)
                dbw.create()
            }
        }

        private fun deleteSessionFromLocalDB(dbw: DBWorkSession, id: Long) {
            dbw.addSelection(AssistantDBContract.Sessions._ID + " LIKE ?")
            dbw.addSelectionArgs(arrayOf<String>(id.toString()))
            dbw.delete()
        }

        private fun deletePartsFromLocalDB(dbw: DBWorkParts, id: Long, bySessionId: Boolean) {

            val selection = if (bySessionId) AssistantDBContract.Parts.SESSION_ID
            else AssistantDBContract.Parts._ID
            dbw.addSelection(selection + " LIKE ?")
            dbw.addSelectionArgs(arrayOf<String>(id.toString()))
            dbw.delete()
        }

        private fun updateSessionFromLocalBD(dbw: DBWorkSession, id: Long, session: SpeechSession) {
            session.id = id
            dbw.run {
                setAllValues(session)
                addSelection(AssistantDBContract.Sessions._ID + " LIKE ?")
                addSelectionArgs(arrayOf<String>(id.toString()))
                update()
            }
        }

        private fun updateOrCreatePartsFromLocalBD(dbw: DBWorkParts, sessionId: Long, parts: ArrayList<Part>) {
            dbw.addSelection(AssistantDBContract.Parts._ID + " LIKE ?")
            dbw.setSessionId(sessionId)
            for (elem in parts) {
                dbw.setAllValues(elem)
                if (elem.id >= 0) {
                    dbw.addSelectionArgs(arrayOf<String>(elem.id.toString()))
                    dbw.update()
                } else {
                    dbw.create()
                }
            }
        }
    }
}