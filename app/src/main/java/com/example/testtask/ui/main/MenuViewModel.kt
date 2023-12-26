package com.example.testtask.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel : ViewModel() {
    private val _coinCount = MutableLiveData<Int>()
    val coinCount: LiveData<Int> = _coinCount

    fun setCoinCount(count: Int) {
        _coinCount.value = count
    }
}
