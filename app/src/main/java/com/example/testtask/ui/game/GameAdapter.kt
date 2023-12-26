package com.example.testtask.ui.game

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtask.R

class GameAdapter(
    private val imageList: Array<Int>,
    private val gameListener: GameListener,
    private val calculateReward: () -> Int
) : RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    private var lastClickedPosition: Int? = null
    private var isComparing = false
    private var isOpened = BooleanArray(imageList.size)
    private var pairs = mutableSetOf<Int>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(position: Int) {
            if (isOpened[position]) {
                imageView.setImageResource(imageList[position])
            } else {
                imageView.setImageResource(R.drawable.card_back)
            }

            imageView.setOnClickListener {
                if (!isOpened[position] && !isComparing) {
                    showImage(position)
                }
            }
        }
    }

    private fun showImage(position: Int) {
        isOpened[position] = true
        notifyItemChanged(position)
        if (lastClickedPosition == null) {
            lastClickedPosition = position
        } else {
            isComparing = true
            Handler(Looper.getMainLooper()).postDelayed({
                compareImages(position)
                isComparing = false
            }, 500)
        }
    }

    private fun compareImages(position: Int) {
        if (imageList[lastClickedPosition!!] == imageList[position]) {
            pairs.add(imageList[position])
        } else {
            isOpened[lastClickedPosition!!] = false
            isOpened[position] = false
            notifyItemChanged(lastClickedPosition!!)
            notifyItemChanged(position)
        }
        lastClickedPosition = null

        if (isGameFinished()) {
            val reward = calculateReward()
            gameListener.onGameFinished(System.currentTimeMillis(), reward)
        }
    }

    private fun isGameFinished(): Boolean {
        return pairs.size == imageList.size / 2
    }
}
