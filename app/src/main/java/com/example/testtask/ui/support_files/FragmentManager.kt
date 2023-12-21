package com.example.testtask.ui.support_files

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.FragmentActivity

class FragmentManager(private val containerId: Int, private val activity: FragmentActivity) {

    fun navigateTo(fragment: Fragment) {
        val transaction: FragmentTransaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(containerId, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
