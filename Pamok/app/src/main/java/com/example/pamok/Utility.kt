package com.example.pamok

import java.io.InputStream

// Auxiliary function for simpler data reading from file
fun InputStream.readTextAndClose(): String {
    return this.bufferedReader(Charsets.UTF_8).use { it.readText() }
}
