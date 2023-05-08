package com.hwonchul.movie

object FileReader {
    fun readStringFromFile(fileName: String): String {
        return javaClass.classLoader!!.getResourceAsStream(fileName)
            .bufferedReader()
            .use { it.readText() }
    }
}