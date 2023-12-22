package com.example.testtask.ui.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R
import com.example.testtask.ui.main.MenuViewModel

class GameScene : AppCompatActivity(), GridAdapter.OnCardClickListener, Game.OnGameUpdateListener {

    private lateinit var viewModel: MenuViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GridAdapter
    private lateinit var game: Game
    private lateinit var stopwatch: Stopwatch
    private lateinit var chronometer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_scene)

        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        recyclerView = findViewById(R.id.gameRecyclerView)
        chronometer = findViewById(R.id.chronometer)

        stopwatch = Stopwatch()
        game = Game(this)

        val gridItems = game.images.shuffled().mapIndexed { index, resId -> GridItem(index, resId) }
        adapter = GridAdapter(this, gridItems, this)

        recyclerView.layoutManager = GridLayoutManager(this, 5)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        // Запускаем секундомер при создании активности
        stopwatch.start()

        // Инициализируем хронометр
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
    }
    override fun onCardClicked(position: Int) {
        val isFlipped = game.isCardFlipped(position)

        // Если карта не перевернута, покажем ее изображение на короткое время, затем перевернем обратно
        if (!isFlipped) {
            // Переворачиваем карту
            game.flipCard(position)
            adapter.notifyItemChanged(position)

            Handler(Looper.getMainLooper()).postDelayed({
                // Переворачиваем обратно через 1000 миллисекунд
                game.flipCard(position)
                adapter.notifyItemChanged(position)
            }, 1000)
        }
    }
    override fun onCardsFlipped(card1: Int, card2: Int, isMatch: Boolean) {
        adapter.notifyItemChanged(card1)
        adapter.notifyItemChanged(card2)
    }

    override fun onGameFinished(moves: Int, elapsedTime: Long, reward: Int) {
        chronometer.stop()
        //Toast.makeText(this, "Игра завершена. Вы получили $reward монет", Toast.LENGTH_SHORT).show()
    }

    override fun isCardFlipped(cardId: Int): Boolean {
        return game.isCardFlipped(cardId)
    }
}
