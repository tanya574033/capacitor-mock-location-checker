package io.github.asephermann.plugins.mocklocationchecker

import android.util.Log

class MockLocationChecker {
    fun echo(value: String): String {
        Log.i("Echo", value)
        return value
    }
}