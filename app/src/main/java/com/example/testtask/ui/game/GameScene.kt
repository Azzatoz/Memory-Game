package com.example.testtask.ui.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R
import com.example.testtask.ui.main.MenuViewModel
import com.example.testtask.ui.support_files.GridAdapter
import com.example.testtask.ui.support_files.GridItem

class GameScene : AppCompatActivity() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_scene)

        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        recyclerView = findViewById(R.id.gameRecyclerView)

        val gridItems = createGridItems().shuffled()
        adapter = GridAdapter(this, gridItems) { clickedItem ->
            // Обработка нажатия на элемент
            clickedItem.flip()  // Перевернуть карту
            adapter.notifyDataSetChanged()  // Уведомить адаптер о изменении данных
        }

        recyclerView.layoutManager = GridLayoutManager(this, 5)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
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
}