package com.nicholasnkk.shiftup.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nicholasnkk.shiftup.R

class SignupActivity : AppCompatActivity() {
    private val TAG = SignupActivity::class.qualifiedName

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private lateinit var signUpBtn: Button
    private lateinit var loginBtn: Button

    private lateinit var switch: Switch


    private lateinit var db: FirebaseFirestore

    private val USER:String = "uid"
    private val MANAGER:String = "manager"

    private lateinit var user: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance();

        emailEt = findViewById(R.id.email_edt_text)
        passwordEt = findViewById(R.id.pass_edt_text)

        loginBtn = findViewById(R.id.login_btn)
        signUpBtn = findViewById(R.id.signup_btn)

        switch = findViewById(R.id.accountType)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch.text = "Manager"
            } else {
                switch.text = "Employee"
            }
        }

        signUpBtn.setOnClickListener {
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user = auth.currentUser!!.uid
                            if (switch.isChecked && !user.isNullOrEmpty())
                                writeManager(Manager(user, true));

                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG)
                                .show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }

        loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun writeManager(manager: Manager) {
        val managerMap = HashMap<String, Any>()
        managerMap["uid"] = manager.uid
        managerMap["owner"] = manager.owner
        db.collection("users").document("managers")
            .set(managerMap)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

}
