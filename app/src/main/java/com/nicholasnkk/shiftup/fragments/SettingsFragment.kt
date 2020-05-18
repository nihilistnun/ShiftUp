package com.nicholasnkk.shiftup.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.activities.LoginActivity
import com.nicholasnkk.shiftup.activities.MainActivity

class SettingsFragment : Fragment() {
    private val TAG = SettingsFragment::class.qualifiedName
    private lateinit var main: MainActivity

    private lateinit var userMessageView: TextView

    private lateinit var logoutBtn: Button
    private lateinit var leaveBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_settings, container, false)
        main = activity as MainActivity

        userMessageView = root.findViewById(R.id.userMessage)
        logoutBtn = root.findViewById(R.id.logoutButton)
        leaveBtn = root.findViewById(R.id.leaveButton)

        val message:String ="You are logged in as " + main.user.firstName + " " + main.user.lastName
        userMessageView.text = message

        if (main.hasGroup)
            leaveBtn.visibility = View.VISIBLE
        else
            leaveBtn.visibility = View.GONE

        logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, LoginActivity::class.java);
            startActivity(intent);
            activity?.finish();
        }

        leaveBtn.setOnClickListener {
            //leave group
            //cannot leave if owner
            if (main.user.owner)
                Toast.makeText(main, "Owners cannot leave", Toast.LENGTH_SHORT).show()
            else {
                //update user info
                resetEmployeeCode()
                //update group info
                removeGroupEmployees()
                main.loadDB()
                Toast.makeText(
                    main,
                    "You have left group " + main.user.groupCode,
                    Toast.LENGTH_SHORT
                ).show()
                leaveBtn.visibility = View.GONE
            }
        }

        return root
    }

    private fun resetEmployeeCode() {
        main.db.collection("users").document(main.uid).update("groupCode", "000000")
    }

    private fun removeGroupEmployees() {
        main.db.collection("groups").document(main.user.groupCode).collection("employees")
            .document(main.user.uid)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "group employee data deleted") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting group employee data", e) }
    }
}
