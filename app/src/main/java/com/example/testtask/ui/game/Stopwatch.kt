package com.example.testtask.ui.game

import android.os.SystemClock

class Stopwatch {

    private var startTime: Long = 0L
    private var isRunning: Boolean = false

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
