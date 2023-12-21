package com.example.testtask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {

    private val _coins = MutableLiveData<Int>()
    val coins: LiveData<Int> get() = _coins

    init {
        _coins.value = 0
    }

    fun addCoins(amount: Int) {
        val currentCoins = _coins.value ?: 0
        _coins.value = currentCoins + amount
    }
}
