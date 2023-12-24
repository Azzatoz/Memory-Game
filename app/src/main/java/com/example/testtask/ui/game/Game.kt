package com.example.testtask.ui.game

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import com.example.testtask.R

class Game(private val onGameUpdateListener: OnGameUpdateListener) {

    private val images: List<Int> = listOf(
        R.mipmap.image1, R.mipmap.image2, R.mipmap.image3,
        R.mipmap.image4, R.mipmap.image5, R.mipmap.image6,
        R.mipmap.image7, R.mipmap.image8, R.mipmap.image9,
        R.mipmap.image10
    )

    private val cards: List<Card> = generateCards()

    val shuffledCards: List<Card> = cards.shuffled()

    var firstCardId: Int? = null
    var secondCardId: Int? = null
    private var moves = 0
    private var startTime: Long = 0
    private var isGameStarted = false
    var isComparing = false
    private var isFlippingAllowed = true

    fun comparison(): Boolean {
        isComparing = true
        isFlippingAllowed = false // Запрещаем переворот карт во время сравнения

        val firstCard = shuffledCards.firstOrNull { it.id == firstCardId }
        val secondCard = shuffledCards.firstOrNull { it.id == secondCardId }
        val returns = firstCard != null && secondCard != null && firstCard.imageResId == secondCard.imageResId
        isComparing = false
        isFlippingAllowed = true // Разрешаем переворот карт после сравнения
        return returns
    }

    private fun generateCards(): List<Card> {
        val cardPairs: List<Pair<Int, Int>> = images.map { Pair(it, it) }
        val allCards: List<Card> =
            cardPairs.flatMap { (first, second) -> listOf(Card(0, first), Card(0, second)) }
        return allCards.shuffled().mapIndexed { index, card -> Card(index, card.imageResId) }
    }
    fun flipCard(cardId: Int) {
        if (!isFlippingAllowed) {
            return  // Если переворот карт запрещен, выходим
        }
        val card = shuffledCards[cardId]
        card.flip()
        onGameUpdateListener.onCardsFlipped(cardId, -1, false)
        if (firstCardId != null && secondCardId != null) { // Если обе карты перевернуты, начинаем сравнение
            Handler(Looper.getMainLooper()).postDelayed({
                val isMatch = comparison() // Выполняем проверку совпадения
                if (isMatch) { // Если совпадение есть, уведомляем адаптер
                    onGameUpdateListener.onCardsFlipped(firstCardId!!, secondCardId!!, true)
                }
                // Сбрасываем выбранные карты и завершаем сравнение
                firstCardId = null
                secondCardId = null
            }, 1000) // Задержка в 1000 миллисекунд (1 секунда)
        }
    }

    fun isCardFlipped(cardId: Int): Boolean {
        if (!isGameStarted) {
            startGame()
        }

        if (firstCardId == null) {
            firstCardId = cardId
            return false
        } else if (secondCardId == null && firstCardId != cardId) {
            secondCardId = cardId

            // Проверяем, совпали ли карты
            val isMatch = comparison()

            // Сбрасываем выбранные карты
            firstCardId = null
            secondCardId = null

            // Если все карты открыты, завершаем игру
            if (isGameFinished()) {
                endGame()
            }

            return isMatch
        }
        // Если код дошел сюда, возвращаем false
        return false
    }

    private fun startGame() {
        isGameStarted = true
        startTime = SystemClock.elapsedRealtime()
    }
    private fun endGame() {
        val elapsedTime = SystemClock.elapsedRealtime() - startTime
        val reward = calculateReward(elapsedTime)
        onGameUpdateListener.onGameFinished(moves, elapsedTime, reward)
    }
    private fun isGameFinished(): Boolean {
        return shuffledCards.all { onGameUpdateListener.isCardFlipped(it.id) }
    }
    private fun calculateReward(elapsedTime: Long): Int {
        val baseReward = 100
        val minReward = 10
        val timeThreshold = 20000
        val penaltyRate = 5
        val penalty = maxOf(0, ((elapsedTime - timeThreshold) / 1000) * penaltyRate).toInt()
        return maxOf(minReward, baseReward - penalty)
    }
    interface OnGameUpdateListener {
        fun onCardsFlipped(card1: Int, card2: Int, isMatch: Boolean) {
            Log.d("Game", "onCardsFlipped: card1=$card1, card2=$card2, isMatch=$isMatch")
        }
        fun onGameFinished(moves: Int, elapsedTime: Long, reward: Int)
        fun isCardFlipped(cardId: Int): Boolean
    }
}
