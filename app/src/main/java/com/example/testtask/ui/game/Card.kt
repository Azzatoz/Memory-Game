package com.example.testtask.ui.game

data class Card(val id: Int, val imageResId: Int, var isFlipped: Boolean = false) {
    fun flip() {
        isFlipped = !isFlipped
    }
}
