package com.luis.caiza.cazarpatos

import android.app.Activity
import java.io.*

class FileInternalManager(private val actividad: Activity) : FileHandler {

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val archivo = actividad.openFileOutput(SHAREDINFO_FILENAME, Activity.MODE_PRIVATE)
        val contenido = "${datosAGrabar.first}\n${datosAGrabar.second}"
        archivo.write(contenido.toByteArray())
        archivo.close()
    }

    override fun ReadInformation(): Pair<String, String> {
        return try {
            val archivo = actividad.openFileInput(SHAREDINFO_FILENAME)
            val reader = BufferedReader(InputStreamReader(archivo))
            val email = reader.readLine() ?: ""
            val clave = reader.readLine() ?: ""
            reader.close()
            email to clave
        } catch (e: Exception) {
            "" to ""
        }
    }
}