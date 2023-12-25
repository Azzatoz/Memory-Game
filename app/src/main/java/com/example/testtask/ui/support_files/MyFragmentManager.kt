package com.example.testtask.ui.support_files

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

class MyFragmentManager(private val containerId: Int, private val activity: FragmentActivity) {

    fun navigateTo(fragment: Fragment) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
