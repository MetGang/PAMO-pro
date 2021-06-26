package com.example.pamok

import java.io.InputStream
import java.nio.charset.Charset

// Auxiliary function for simpler data reading from file
fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}
