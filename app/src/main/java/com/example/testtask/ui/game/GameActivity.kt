package com.example.testtask.ui.game

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R
import com.example.testtask.ui.support_files.CurrencyManager

class GameActivity : AppCompatActivity(), GameListener {

    private lateinit var chronometer: Chronometer
    private lateinit var currencyTextView: TextView
    private lateinit var gameRecyclerView: RecyclerView
    private lateinit var descriptionTextView: TextView
    private lateinit var autoFinishButton: Button
    private lateinit var gameAdapter: GameAdapter
    private lateinit var currencyManager: CurrencyManager

    private val originalImages = arrayOf(
        R.mipmap.image1, R.mipmap.image2, R.mipmap.image3, R.mipmap.image4, R.mipmap.image5,
        R.mipmap.image6, R.mipmap.image7, R.mipmap.image8, R.mipmap.image9, R.mipmap.image10
    )

    private val images = originalImages + originalImages

    private var doubleRewardButtonClicked = false
    private var baseReward: Int = 100
    private var calculatedReward: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_scene)

        chronometer = findViewById(R.id.chronometer)
        currencyTextView = findViewById(R.id.currencyTextView)
        gameRecyclerView = findViewById(R.id.gameRecyclerView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        autoFinishButton = findViewById(R.id.endGameButton)

        images.shuffle()

        gameRecyclerView.layoutManager = GridLayoutManager(this, 5)
        gameAdapter = GameAdapter(images, this, ::calculateReward)
        gameRecyclerView.adapter = gameAdapter

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        // Инициализация CurrencyManager
        currencyManager = CurrencyManager(this)

        // Загрузка значения валюты при запуске активности
        loadCurrency()

        autoFinishButton.setOnClickListener {
            // Вызываем метод, который завершает игру автоматически
            autoFinishGame()
        }
    }

    private fun loadCurrency() {
        // Получение значения валюты из CurrencyManager
        val currency = currencyManager.getCurrency()
        currencyTextView.text = getString(R.string.coins_earned, currency.toString())
    }

    private fun autoFinishGame() {
        Log.d("com.example.testtask.ui.game.GameActivity", "Auto-finishing the game")
        val elapsedTime = System.currentTimeMillis() - chronometer.base
        baseReward = calculateReward(elapsedTime)

        // Удваиваем награду, если кнопка была нажата
        val doubledReward = if (doubleRewardButtonClicked) 2 * baseReward else baseReward

        // Сохраняем значение валюты при автоматическом завершении игры
        calculatedReward = doubledReward

        // Вызываем метод onGameFinished и передаем ему время и награду
        onGameFinished(elapsedTime, doubledReward)
    }

    override fun onGameFinished(elapsedTime: Long, reward: Int) {
        // Если кнопка удвоения была нажата, удваиваем награду
        val calculatedReward = if (doubleRewardButtonClicked) 2 * reward else reward

        // Сохраняем значение валюты
        calculatedReward.let { currencyManager.addCurrency(it) }

        // Обновляем UI
        currencyTextView.text = getString(R.string.coins_earned, currencyManager.getCurrency().toString())

        // Добавим логи
        Log.d("GameAdapter", "Game finished! Elapsed time: $elapsedTime, Original reward: $baseReward, Doubled reward: $calculatedReward")

        // Останавливаем секундомер
        stopChronometer()

        // Показываем EndGamePopup, передавая количество монет в награде
        showEndGamePopup(calculatedReward)
    }

    override fun showEndGamePopup(coinsEarned: Int) {
        Log.d("com.example.testtask.ui.game.GameActivity", "Showing EndGamePopup with coinsEarned: $coinsEarned")
        // Отображаем EndGamePopup, передавая количество монет в награде
        val endGamePopup = EndGamePopup.newInstance(coinsEarned)
        endGamePopup.show(supportFragmentManager, EndGamePopup.TAG)
    }

    fun onHomeButtonClicked() {
        // Если игра завершена и кнопка Home была нажата, передаем основное значение валюты в основной кошелек
        if (gameAdapter.isGameFinished()) {
            currencyManager.addCurrency(calculatedReward)
            currencyTextView.text = getString(R.string.coins_earned, currencyManager.getCurrency().toString())
        }
    }

    private fun stopChronometer() {
        // Останавливаем секундомер
        chronometer.stop()
    }

    private fun calculateReward(elapsedTime: Long): Int {
        val maxReward = 100
        val minReward = 10
        val timeThreshold = 20 * 1000

        // Используем min для предотвращения отрицательных значений
        return (maxReward - ((elapsedTime - timeThreshold).coerceAtLeast(0) / 1000L * 5)).coerceAtLeast(
            minReward.toLong()
        ).toInt()
    }
}
