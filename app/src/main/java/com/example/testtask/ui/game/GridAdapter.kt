package com.example.testtask.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R

class GridAdapter(
    private val context: Context,
    private val items: List<Card>,
    private val onCardClickListener: OnCardClickListener
) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onCardClickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardButton: ImageButton = itemView.findViewById(R.id.cardButton)

        fun bind(item: Card, onCardClickListener: OnCardClickListener) {
            // Обработчик нажатия
            cardButton.setOnClickListener {
                // Вызываем обработчик клика с ID карты
                onCardClickListener.onCardClicked(bindingAdapterPosition)
            }

            // Устанавливаем изображение карты
            updateCardImage(item.isFlipped)
        }

        private fun updateCardImage(isFlipped: Boolean) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val card = items[position]
                if (isFlipped) {
                    cardButton.setImageResource(card.imageResId)
                } else {
                    cardButton.setImageResource(android.R.color.transparent)  // Очистим изображение
                    cardButton.setBackgroundResource(R.drawable.card_back)
                }
            }
        }
    }

    interface OnCardClickListener {
        fun onCardClicked(cardId: Int)
    }
}
