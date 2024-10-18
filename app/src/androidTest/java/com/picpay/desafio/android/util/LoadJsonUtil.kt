package com.picpay.desafio.android.util

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import java.io.IOException
import java.io.InputStream


fun loadJSONFromAsset(filename:String): String {
    val context: Context = InstrumentationRegistry.getInstrumentation().context
    var json: String? = null
    try {
        val input: InputStream = context.assets.open(filename)
        val size = input.available()
        val buffer = ByteArray(size)
        input.read(buffer)
        input.close()
        json = String(buffer, charset("UTF-8"))
    } catch (ex: IOException) {
        ex.printStackTrace()
        return ""
    }
    return json
}