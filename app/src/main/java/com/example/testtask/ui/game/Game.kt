package com.example.testtask.ui.game

import android.os.SystemClock
import com.example.testtask.R

class Game(private val onGameUpdateListener: OnGameUpdateListener) {

    val images: List<Int> = listOf(
        R.mipmap.image1, R.mipmap.image2, R.mipmap.image3,
        R.mipmap.image4, R.mipmap.image5, R.mipmap.image6,
        R.mipmap.image7, R.mipmap.image8, R.mipmap.image9,
        R.mipmap.image10
    )

    private val shuffledImages: List<GridItem> = images.mapIndexed { index, resId -> GridItem(index, resId) }.shuffled()

    private var firstCardId: Int? = null
    private var secondCardId: Int? = null
    private var moves = 0
    private var startTime: Long = 0
    private var isGameStarted = false


    fun flipCard(cardId: Int) {
        val card = shuffledImages[cardId]
        card.flip()
        onGameUpdateListener.onCardsFlipped(cardId, -1, false) // Обновляем только одну карту
    }

    fun isCardFlipped(cardId: Int): Boolean {
        if (!isGameStarted) {
            startGame()
        }

        if (firstCardId == null) {
            firstCardId = cardId
            return false // Возвращаем false, так как карта еще не перевернута
        } else if (secondCardId == null && firstCardId != cardId) {
            secondCardId = cardId
            moves++

            // Проверяем, совпали ли карты
            val isMatch = checkMatch()

            // Сбрасываем выбранные карты
            firstCardId = null
            secondCardId = null

            // Если все карты открыты, завершаем игру
            if (isGameFinished()) {
                endGame()
            }

            // Возвращаем результат сравнения карт
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

    private fun calculateReward(elapsedTime: Long): Int {
        val baseReward = 100
        val minReward = 10
        val timeThreshold = 20000 // Порог времени в миллисекундах
        val penaltyRate = 5

        // Вычисляем штраф за каждую последующую секунду сверх порога
        val penalty = maxOf(0, ((elapsedTime - timeThreshold) / 1000) * penaltyRate).toInt()

        // Вычисляем итоговую награду
        return maxOf(minReward, baseReward - penalty)
    }

    private fun checkMatch(): Boolean {
        val firstIndex = shuffledImages.indexOfFirst { it.id == firstCardId }
        val secondIndex = shuffledImages.indexOfFirst { it.id == secondCardId }
        return firstIndex != -1 && secondIndex != -1 && firstIndex / 2 == secondIndex / 2
    }

    private fun isGameFinished(): Boolean {
        return shuffledImages.all { onGameUpdateListener.isCardFlipped(it.id) }
    }


    interface OnGameUpdateListener {
        fun onCardsFlipped(card1: Int, card2: Int, isMatch: Boolean)
        fun onGameFinished(moves: Int, elapsedTime: Long, reward: Int)
        fun isCardFlipped(cardId: Int): Boolean
    }
}
