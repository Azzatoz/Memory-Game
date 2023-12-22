package com.example.testtask.ui.game

data class GridItem(val id: Int, val imageResId: Int, var isFlipped: Boolean = false) {
    fun flip() {
        isFlipped = !isFlipped
    }
}
