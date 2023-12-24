package com.example.testtask.ui.game

interface OnGameUpdateListener {

    fun onCardsFlipped(card1: Int, card2: Int, isMatch: Boolean) {}
    fun onGameFinished(moves: Int, elapsedTime: Long, reward: Int)
    fun isCardFlipped(cardId: Int): Boolean
    fun onCardClicked(cardId: Int)
}