package com.example.testtask.ui.game

import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R
import com.example.testtask.ui.main.MenuViewModel
import com.example.testtask.ui.support_files.GridAdapter
import com.example.testtask.ui.support_files.GridItem
import com.example.testtask.ui.support_files.Stopwatch

class GameScene : AppCompatActivity() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GridAdapter
    private lateinit var stopwatch: Stopwatch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_scene)

        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        recyclerView = findViewById(R.id.gameRecyclerView)
        val chronometer: Chronometer = findViewById(R.id.chronometer)

        stopwatch = Stopwatch()

        val gridItems = createGridItems().shuffled()
        adapter = GridAdapter(this, gridItems) { clickedItem ->
            // Обработка нажатия на элемент
            clickedItem.flip()  // Перевернуть карту
            adapter.notifyDataSetChanged()  // Уведомить адаптер о изменении данных
            updateChronometer(chronometer)  // Обновить секундомер
        }

        recyclerView.layoutManager = GridLayoutManager(this, 5)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        // Запускаем секундомер при создании активности
        stopwatch.start()
    }

    private fun createGridItems(): List<GridItem> {
        val itemList = mutableListOf<GridItem>()

        val imageResIds = listOf(
            R.mipmap.image1, R.mipmap.image2, R.mipmap.image3,
            R.mipmap.image4, R.mipmap.image5, R.mipmap.image6,
            R.mipmap.image7, R.mipmap.image8, R.mipmap.image9,
            R.mipmap.image10
        )

        for (i in 1..2) {
            for (imageResId in imageResIds) {
                val item = GridItem(i, imageResId)
                itemList.add(item)
            }
        }
        return itemList.shuffled()
    }
    private fun updateChronometer(chronometer: Chronometer) {
        chronometer.base = SystemClock.elapsedRealtime() - stopwatch.elapsedTime()
        chronometer.start()

        // Проверяем, завершилась ли игра
        if (stopwatch.isRunning && stopwatch.elapsedTime() >= 20000) {
            stopwatch.stop()  // Останавливаем секундомер

            // Вычисляем награду
            val reward = calculateReward(stopwatch.elapsedTime())
            // Добавляем награду к общему количеству монет
            viewModel.addCoins(reward)

            // Выводим сообщение с результатами
            //Toast.makeText(this, "Вы получили $reward монет", Toast.LENGTH_SHORT).show()
        }
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
}