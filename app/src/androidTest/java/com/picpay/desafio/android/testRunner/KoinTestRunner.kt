package com.picpay.desafio.android.testRunner

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.test.runner.AndroidJUnitRunner
import com.picpay.desafio.android.TestApplication

class KoinTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        classLoader: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(classLoader, TestApplication::class.java.name, context)
    }
}