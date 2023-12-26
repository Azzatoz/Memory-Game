package com.example.testtask.ui.support_files

import android.content.Context
import android.content.SharedPreferences

class CurrencyManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("com.example.testtask.prefs", Context.MODE_PRIVATE)
    private val currencyKey = "currency"

    fun getCurrency(): Int {
        return prefs.getInt(currencyKey, 0)
    }

    fun setCurrency(amount: Int) {
        prefs.edit().putInt(currencyKey, amount).apply()
    }

    fun addCurrency(amount: Int) {
        val currentCurrency = getCurrency()
        setCurrency(currentCurrency + amount)
    }
}
