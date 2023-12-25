package com.example.testtask.ui.game

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R

class GameActivity : AppCompatActivity(), GameListener {

    private lateinit var chronometer: Chronometer
    private lateinit var currencyTextView: TextView
    private lateinit var gameRecyclerView: RecyclerView
    private lateinit var descriptionTextView: TextView
    private lateinit var gameAdapter: GameAdapter

    // Имя файла SharedPreferences
    private val PREFS_FILENAME = "com.example.testtask.prefs"
    // Ключ для сохранения и получения значения валюты
    private val CURRENCY_KEY = "currency"

    private val originalImages = arrayOf(
        R.mipmap.image1, R.mipmap.image2, R.mipmap.image3, R.mipmap.image4, R.mipmap.image5,
        R.mipmap.image6, R.mipmap.image7, R.mipmap.image8, R.mipmap.image9, R.mipmap.image10
    )

    private val images = originalImages + originalImages

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_scene)

        chronometer = findViewById(R.id.chronometer)
        currencyTextView = findViewById(R.id.currencyTextView)
        gameRecyclerView = findViewById(R.id.gameRecyclerView)
        descriptionTextView = findViewById(R.id.descriptionTextView)

        images.shuffle()

        gameRecyclerView.layoutManager = GridLayoutManager(this, 5)
        gameAdapter = GameAdapter(images, this)
        gameRecyclerView.adapter = gameAdapter

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        // Загрузка значения валюты при запуске активности
        loadCurrency()
    }

    private fun updateCurrency(newCurrencyValue: Int) {
        // Сохранение значения валюты в SharedPreferences
        val prefs: SharedPreferences = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putInt(CURRENCY_KEY, newCurrencyValue)
        editor.apply()
    }

    private fun loadCurrency() {
        // Получение значения валюты из SharedPreferences
        val prefs: SharedPreferences = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val currency = prefs.getInt(CURRENCY_KEY, 0)
        currencyTextView.text = getString(R.string.coins_earned, currency.toString())
    }

    override fun onGameFinished(elapsedTime: Long, reward: Int) {
        updateCurrency(reward)
        currencyTextView.text = getString(R.string.coins_earned, reward.toString())

        // Вызываем методы для отображения EndGamePopup и остановки секундомера
        showEndGamePopup(reward)
        stopChronometer()
    }

    override fun showEndGamePopup(coinsEarned: Int) {
        // Отображаем EndGamePopup, передавая количество монет в награде
        val endGamePopup = EndGamePopup.newInstance(coinsEarned)
        endGamePopup.show(supportFragmentManager, EndGamePopup.TAG)
    }

    private fun stopChronometer() {
        // Останавливаем секундомер
        chronometer.stop()
    }
}
