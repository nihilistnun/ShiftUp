package com.nicholasnkk.shiftup.ui.login.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.ui.login.activities.LoginActivity

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
