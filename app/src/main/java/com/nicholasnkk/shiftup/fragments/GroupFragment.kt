package com.nicholasnkk.shiftup.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.RoleDialog
import com.nicholasnkk.shiftup.activities.MainActivity
import com.nicholasnkk.shiftup.recyclers.EmployeeListAdapter
import com.nicholasnkk.shiftup.recyclers.RoleListAdapter


class GroupFragment : Fragment(){
    private val TAG = GroupFragment::class.qualifiedName
    private lateinit var main: MainActivity

    private lateinit var codeView: TextView
    private lateinit var copyBtn: Button
    private lateinit var addBtn: Button

    private lateinit var codeEt: EditText
    private lateinit var joinBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = activity as MainActivity
        if (main.hasGroup) {
            val root = inflater.inflate(R.layout.fragment_employees, container, false)

            codeView = root.findViewById(R.id.code_view)
            copyBtn = root.findViewById(R.id.copyButton)
            addBtn = root.findViewById(R.id.addRoleButton)

            val codeViewString: String = "Group code: " + main.user.groupCode
            codeView.text = codeViewString

            copyBtn.setOnClickListener {
                val myClipboard: ClipboardManager =
                    requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("Group code", main.user.groupCode)
                myClipboard.primaryClip = clip
                Toast.makeText(main, "Group code copied", Toast.LENGTH_SHORT).show()
            }

            addBtn.setOnClickListener {
                val dialogFragment = RoleDialog()
                dialogFragment.show(main.supportFragmentManager, "dialog")
            }

            return root
        } else {
            var root = inflater.inflate(R.layout.fragment_join, container, false)

            codeEt = root.findViewById(R.id.code_edt_text)
            joinBtn = root.findViewById(R.id.joinButton)

            joinBtn.setOnClickListener {
                val code: String = codeEt.text.toString()
                if (TextUtils.isEmpty(code) || code.length != 6) {
                    Toast.makeText(main, "Please enter a 6 digit code", Toast.LENGTH_SHORT).show()
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
                        //update user fields
                        updateEmployeeCode(code)
                        //update group employee list
                        updateGroupEmployees(code)
                        main.loadDB()
                        main.hasGroup = true
                        //inflate new fragment
                        Toast.makeText(
                            main,
                            "You have joined group $code",
                            Toast.LENGTH_SHORT
                        ).show()
                        view?.let { main.hideKeyboard(it) }
                    } else {
                        Toast.makeText(main, "Group code is invalid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        if (main.hasGroup) {
            val employeeRecycler: RecyclerView = view.findViewById(R.id.employee_recycler_view)
            val roleRecycler: RecyclerView = view.findViewById(R.id.role_recycler_view)

            employeeRecycler.isNestedScrollingEnabled = false
            employeeRecycler.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = EmployeeListAdapter(main.groupEmployees)
            }

            roleRecycler.isNestedScrollingEnabled = false
            roleRecycler.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = RoleListAdapter(main, main.groupRoles)
            }
        }
    }

    private fun updateEmployeeCode(code: String) {
        main.db.collection("users").document(main.uid).update("groupCode", code)
    }

    private fun updateGroupEmployees(code: String) {
        main.db.collection("groups").document(code).collection("employees").document(main.user.uid)
            .set(main.user)
            .addOnSuccessListener { Log.d(TAG, "Group employee successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing group employee", e) }
    }

}
