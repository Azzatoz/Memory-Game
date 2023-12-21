package com.example.testtask.ui.support_files

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R


class GridAdapter(private val context: Context, private val items: List<GridItem>, private val onItemClick: (GridItem) -> Unit) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onItemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardButton: ImageButton = itemView.findViewById(R.id.cardButton)

        fun bind(item: GridItem, onItemClick: (GridItem) -> Unit) {
            // Обработчик нажатия
            cardButton.setOnClickListener {
                onItemClick(item)
            }

            // Устанавливаем изображение карты
            if (item.isFlipped) {
                cardButton.setImageResource(item.imageResId)
            } else {
                cardButton.setBackgroundResource(R.drawable.card_back)
            }
        }
    }
}