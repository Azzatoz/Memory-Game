package com.example.testtask.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testtask.R
import com.example.testtask.ui.additional_fragments.PrivacyPoliceFragment
import com.example.testtask.ui.additional_fragments.SettingsFragment
import com.example.testtask.ui.game.GameActivity
import com.example.testtask.ui.support_files.CurrencyManager
import com.example.testtask.ui.support_files.MyFragmentManager

// MenuFragment.kt
class MenuFragment : Fragment() {

    private lateinit var startGameButton: Button
    private lateinit var settingsButton: ImageButton
    private lateinit var privacyPolicyButton: ImageButton
    private lateinit var coinCountTextView: TextView
    private lateinit var myFragmentManager: MyFragmentManager

    private val startGameForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                // Получаем данные из GameActivity и обновляем значение валюты
                val calculatedReward = result.data?.getIntExtra("calculatedReward", 0) ?: 0
                val currentCoinCount = CurrencyManager(requireContext()).getCurrency()
                CurrencyManager(requireContext()).setCurrency(currentCoinCount + calculatedReward)
                updateCoinCountText(currentCoinCount + calculatedReward)
            }
        }

    private fun updateCoinCountText(coinCount: Int) {
        coinCountTextView.text = getString(R.string.coins_earned, coinCount.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        startGameButton = view.findViewById(R.id.startGameButton)
        settingsButton = view.findViewById(R.id.settingsButton)
        privacyPolicyButton = view.findViewById(R.id.privacyPolicyButton)
        coinCountTextView = view.findViewById(R.id.coinCount)

        // Начальная инициализация значения валюты
        val initialCoinCount = CurrencyManager(requireContext()).getCurrency()
        updateCoinCountText(initialCoinCount)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myFragmentManager = MyFragmentManager(R.id.fragmentContainer, requireActivity())

        startGameButton.setOnClickListener {
            // Получаем текущее значение валюты из CurrencyManager
            val currentCoinCount = CurrencyManager(requireContext()).getCurrency()

            // Передаем его в GameActivity
            val intent = Intent(requireContext(), GameActivity::class.java)
            intent.putExtra("currentCoinCount", currentCoinCount)
            startGameForResult.launch(intent)
        }

        settingsButton.setOnClickListener {
            myFragmentManager.navigateTo(SettingsFragment())
        }

        privacyPolicyButton.setOnClickListener {
            myFragmentManager.navigateTo(PrivacyPoliceFragment())
        }
    }
}
