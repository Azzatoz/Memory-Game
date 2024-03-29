package com.example.testtask.ui.game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.testtask.R
import com.example.testtask.ui.main.MainActivity

class EndGamePopup : DialogFragment() {

    private var coinsEarned: Int = 0
    private var doubleRewardButtonClicked = false
    private var endGameListener: EndGameListener? = null

    interface EndGameListener {
        fun leaveout(coinsEarned: Int)
    }

    companion object {
        const val TAG = "EndGamePopup"
        private const val COINS_EARNED_KEY = "coinsEarned"

        fun newInstance(coinsEarned: Int, endGameListener: EndGameListener): EndGamePopup {
            val fragment = EndGamePopup()
            val args = Bundle()
            args.putInt(COINS_EARNED_KEY, coinsEarned)
            fragment.arguments = args
            fragment.endGameListener = endGameListener
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog?.window?.setLayout(
            resources.getDimensionPixelSize(R.dimen.popup_width),
            ViewGroup.LayoutParams.WRAP_CONTENT);

        val view = inflater.inflate(R.layout.fragment_end_game_popup, container, false)

        val doubleRewardButton: Button = view.findViewById(R.id.doubleRewardButton)
        val homeButton: ImageButton = view.findViewById(R.id.homeButton)

        // Получение данных из аргументов
        coinsEarned = requireArguments().getInt(COINS_EARNED_KEY, 10)

        doubleRewardButton.setOnClickListener {
            if (!doubleRewardButtonClicked) {
                // Удвоение награды и обновление UI
                coinsEarned *= 2
                updateUI(coinsEarned)

                // Помечаем кнопку как уже нажатую
                doubleRewardButtonClicked = true
            }
        }

        homeButton.setOnClickListener {
            dismiss()

            // Перенаправляем в меню (MainActivity)
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Уведомляем GameActivity, что игра завершена
            endGameListener?.leaveout(coinsEarned)
        }

        return view
    }
    override fun onStart() {
        super.onStart()
        updateUI(coinsEarned)
    }

    private fun updateUI(coinsEarned: Int) {
        // Обновление текста с количеством заработанных монет
        val rewardTextView: TextView? = view?.findViewById(R.id.rewardText)
        val rewardString = getString(R.string.reward_text, coinsEarned.toString())
        rewardTextView?.text = rewardString

    }

}
