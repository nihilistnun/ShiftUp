package com.nicholasnkk.shiftup.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.activities.LoginActivity
import com.nicholasnkk.shiftup.activities.MainActivity

class SettingsFragment : Fragment() {
    private val TAG = SettingsFragment::class.qualifiedName
    private lateinit var main: MainActivity

    private lateinit var logoutBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        main = activity as MainActivity

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
