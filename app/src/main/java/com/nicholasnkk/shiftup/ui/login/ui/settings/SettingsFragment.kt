package com.nicholasnkk.shiftup.ui.login.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.ui.login.LoginActivity
import com.nicholasnkk.shiftup.ui.login.MainActivity

class SettingsFragment : Fragment() {

    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)

        logoutBtn = root.findViewById(R.id.logoutButton)
        logoutBtn.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java);
            startActivity(intent);
            activity?.finish();
        }

        return root
    }
}
