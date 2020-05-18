package com.nicholasnkk.shiftup.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nicholasnkk.shiftup.Employee
import com.nicholasnkk.shiftup.GroupCode
import com.nicholasnkk.shiftup.R

class SignUpActivity : AppCompatActivity() {
    private val TAG = SignUpActivity::class.qualifiedName

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var fNameEt: EditText
    private lateinit var lNameEt: EditText

    private lateinit var signUpBtn: Button
    private lateinit var loginBtn: Button

    private lateinit var switch: Switch


    private lateinit var db: FirebaseFirestore

    private var groupCodes: ArrayList<String> = arrayListOf();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance();

        //load group codes list
        db.collection("groupCodes").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (result in task.result!!) {
                    groupCodes.add(result.id)
                }
            } else {
                Log.d(TAG, "Cached get failed: ", task.exception)
            }
        }

        emailEt = findViewById(R.id.email_edt_text)
        passwordEt = findViewById(R.id.pass_edt_text)
        fNameEt = findViewById(R.id.fName_edt_text)
        lNameEt = findViewById(R.id.lName_edt_text)

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
            var fName: String = fNameEt.text.toString()
            var lName: String = lNameEt.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(fName) || TextUtils.isEmpty(
                    lName
                )
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var user = auth.currentUser!!.uid
                            //check if manager
                            if (switch.isChecked && !user.isNullOrEmpty()) {
                                //generate unique group code
                                val groupCode: String = uniqueCode()
                                writeGroupCode(
                                    GroupCode(
                                        groupCode,
                                        user
                                    )
                                )
                                val employee =
                                    Employee(
                                        user, groupCode, fName, lName,
                                        manager = true,
                                        owner = true
                                    )
                                writeEmployee(employee)
                                writeGroup(groupCode, employee)
                            } else
                                writeEmployee(
                                    Employee(
                                        user, "000000", fName, lName,
                                        manager = false,
                                        owner = false
                                    )
                                )
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

    private fun writeEmployee(employee: Employee) {
        val employeeMap = HashMap<String, Any>()
        employeeMap["uid"] = employee.uid
        employeeMap["groupCode"] = employee.groupCode
        employeeMap["firstName"] = employee.firstName
        employeeMap["lastName"] = employee.lastName
        employeeMap["manager"] = employee.manager
        employeeMap["owner"] = employee.owner
        db.collection("users").document(employee.uid)
            .set(employeeMap)
            .addOnSuccessListener { Log.d(TAG, "Employee successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing employee", e) }
    }

    private fun writeGroupCode(groupCode: GroupCode) {
        val groupCodeMap = HashMap<String, Any>()
        groupCodeMap["groupCode"] = groupCode.groupCode
        groupCodeMap["uid"] = groupCode.uid
        db.collection("groupCodes").document(groupCode.groupCode.toString())
            .set(groupCodeMap)
            .addOnSuccessListener { Log.d(TAG, "Group code successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing group code", e) }
    }

    private fun uniqueCode(): String {
        var groupCode: String = (1..999999).random().toString().padStart(6, '0')
        while (groupCodes.indexOf(groupCode) != -1)
            groupCode = (1..999999).random().toString().padStart(6, '0')
        Log.d(TAG, "generated $groupCode")
        return groupCode
    }

    private fun writeGroup(groupCode: String, employee: Employee) {
        val groupMap = HashMap<String, Any>()
        groupMap["roleNumber"] = 0
        db.collection("groups").document(groupCode)
            .set(groupMap)
            .addOnSuccessListener {
                Log.d(TAG, "Group successfully written!")
                writeGroupEmployee(groupCode, employee)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing group", e) }
    }

    private fun writeGroupEmployee(groupCode: String, employee: Employee) {
        val employeeMap = HashMap<String, Any>()
        employeeMap["uid"] = employee.uid
        employeeMap["groupCode"] = employee.groupCode
        employeeMap["firstName"] = employee.firstName
        employeeMap["lastName"] = employee.lastName
        employeeMap["manager"] = employee.manager
        employeeMap["owner"] = employee.owner
        db.collection("groups").document(groupCode).collection("employees").document(employee.uid)
            .set(employeeMap)
            .addOnSuccessListener { Log.d(TAG, "Group employee successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing group employee", e) }
    }
}
