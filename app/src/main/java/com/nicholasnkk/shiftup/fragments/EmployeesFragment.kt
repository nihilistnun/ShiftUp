package com.nicholasnkk.shiftup.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.activities.MainActivity

class EmployeesFragment : Fragment() {
    private val TAG = EmployeesFragment::class.qualifiedName
    private lateinit var main: MainActivity

    private lateinit var codeEt: EditText
    private lateinit var joinBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = activity as MainActivity
        Log.d(TAG, main.group.employeeList[0].firstName)

        if (main.hasGroup) {
            val root = inflater.inflate(R.layout.fragment_settings, container, false)
//replace with employee list xml
            return root
        } else {
            val root = inflater.inflate(R.layout.fragment_employees, container, false)

            codeEt = root.findViewById(R.id.code_edt_text)
            joinBtn = root.findViewById(R.id.joinButton)

            joinBtn.setOnClickListener {
                var code: String = codeEt.text.toString().padStart(6, '0')
                if (TextUtils.isEmpty(code) || code.length != 6) {
                    Toast.makeText(main, "Please enter a 6 digit code", Toast.LENGTH_LONG).show()
                } else {
                    var valid: Boolean = false
                    for (c in main.groupCodes) {
                        if (code == c) {
                            valid = true
                            break
                        }
                    }
                    if (valid) {
                        //join the group
                        //update employee fields

                        //update group employee list

                    } else {
                        Toast.makeText(main, "Group code is invalid", Toast.LENGTH_LONG).show()
                    }
                }
            }
            return root
        }
    }

    private fun updateEmployeeCode(code: String) {
        main.db.collection("users").document(main.uid).update("groupCode", code)
    }

    private fun updateGroupEmployees(code: String) {
//        main.db.collection("groups").document(code).get("employeeList").addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                var data = task.result?.data
//                if (!data.isNullOrEmpty()) {
//                    group = Group(
//                        data["groupCode"] as String,
//                        data["uid"] as String,
//                        data["employeeList"] as java.util.ArrayList<Employee>,
//                        data["roleList"] as java.util.ArrayList<Role>,
//                        data["rotaList"] as java.util.ArrayList<Rota>
//                    )
//                }
//                Log.d(TAG, "Group loaded")
//                loadRest()
//            } else {
//                Log.d(TAG, "user load failed: ", task.exception)
//            }
//        }
    }
}
