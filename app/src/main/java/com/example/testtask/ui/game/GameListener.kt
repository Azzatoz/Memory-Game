package com.example.testtask.ui.game

interface GameListener {
    fun onGameFinished(elapsedTime: Long, reward: Int)
    fun showEndGamePopup(coinsEarned: Int)
}
