package com.example.testtask.ui.game

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
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

        // Используем shuffledImages из Game вместо создания нового списка
        adapter = GridAdapter(this, game.shuffledCards, this)

        recyclerView.layoutManager = GridLayoutManager(this, 5)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        stopwatch.start()

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()


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
    override fun onCardClicked(cardId: Int) {
        Log.d("game", "onCardClicked: cardId=$cardId, isComparing=${game.isComparing}")

        // Проверяем, идет ли в данный момент сравнение карт
        if (!game.isComparing) {
            Log.d("game", "onCardClicked: Not comparing")

            // Если карта не перевернута
            if (!game.isCardFlipped(cardId)) {
                // Переворачиваем карту
                game.flipCard(cardId)
                adapter.notifyItemChanged(cardId)
                // Если это вторая карта, и она не совпадает с первой
                if (game.secondCardId != null && game.firstCardId != cardId) {
                    Log.d("game", "onCardClicked: Second card and not the same")
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Выполняем проверку совпадения
                        val isMatch = game.comparison()
                        Log.d("game", "onCardClicked: Checking match: isMatch=$isMatch")

                        // Если совпадение есть, уведомляем адаптер
                        if (isMatch) {
                            adapter.notifyItemChanged(cardId)
                            adapter.notifyItemChanged(game.secondCardId!!)
                            Log.d("game", "onCardClicked: Matching cards")
                        } else {
                            // Если нет совпадения, переворачиваем обратно
                            game.flipCard(cardId)
                            adapter.notifyItemChanged(cardId)
                            game.flipCard(game.secondCardId!!)
                            adapter.notifyItemChanged(game.secondCardId!!)
                            Log.d("game", "onCardClicked: Not matching, flipping back")
                        }
                        Log.d("game", "onCardClicked: Comparison ended")
                    }, 1000) // Задержка в 1000 миллисекунд (1 секунда)
                }
            }
        }
    }
}
