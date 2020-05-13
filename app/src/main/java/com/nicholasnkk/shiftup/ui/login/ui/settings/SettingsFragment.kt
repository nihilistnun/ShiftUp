package com.nicholasnkk.shiftup.ui.login.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.nicholasnkk.shiftup.R

class SettingsFragment : Fragment() {

    private lateinit var notificationsViewModel: SettingssViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(SettingssViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        val textView: TextView = root.findViewById(R.id.text_settings)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
