package com.example.testtask.ui.support_files

import android.os.SystemClock

class Stopwatch {

    private var startTime: Long = 0L
    var isRunning: Boolean = false

    fun start() {
        if (!isRunning) {
            startTime = SystemClock.elapsedRealtime()
            isRunning = true
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
        }
    }

    fun elapsedTime(): Long {
        return if (isRunning) {
            SystemClock.elapsedRealtime() - startTime
        } else {
            0L
        }
    }
}
