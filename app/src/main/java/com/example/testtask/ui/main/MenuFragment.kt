package com.example.testtask.ui.main
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.testtask.R
import com.example.testtask.ui.additional_fragments.PrivacyPoliceFragment
import com.example.testtask.ui.additional_fragments.SettingsFragment
import com.example.testtask.ui.game.GameActivity
import com.example.testtask.ui.support_files.MyFragmentManager

class MenuFragment : Fragment() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var startGameButton: Button
    private lateinit var settingsButton: ImageButton
    private lateinit var privacyPolicyButton: ImageButton
    private lateinit var coinCountTextView: TextView
    private lateinit var myFragmentManager: MyFragmentManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        startGameButton = view.findViewById(R.id.startGameButton)
        settingsButton = view.findViewById(R.id.settingsButton)
        privacyPolicyButton = view.findViewById(R.id.privacyPolicyButton)
        coinCountTextView = view.findViewById(R.id.coinCount)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

        // Наблюдаем за изменениями количества монет
        viewModel.coinCount.observe(viewLifecycleOwner) { coinCount ->
            coinCountTextView.text = getString(R.string.coins_earned, coinCount.toString())
        }

        myFragmentManager = MyFragmentManager(R.id.fragmentContainer, requireActivity())

        startGameButton.setOnClickListener {
            startActivity(Intent(requireContext(), GameActivity::class.java))
        }

        settingsButton.setOnClickListener {
            myFragmentManager.navigateTo(SettingsFragment())
        }

        privacyPolicyButton.setOnClickListener {
            myFragmentManager.navigateTo(PrivacyPoliceFragment())
        }
    }
}
