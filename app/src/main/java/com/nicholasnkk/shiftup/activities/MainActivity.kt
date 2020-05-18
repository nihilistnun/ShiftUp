package com.nicholasnkk.shiftup.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nicholasnkk.shiftup.*

class MainActivity : AppCompatActivity(), RoleDialog.DialogListener, ShiftDialog.DialogListener {
    private val TAG = MainActivity::class.qualifiedName

    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    lateinit var uid: String
    lateinit var user: Employee
    var hasGroup: Boolean = false
    lateinit var group: Group
    var groupEmployees: ArrayList<Employee> = arrayListOf();
    var groupShifts: ArrayList<Shift> = arrayListOf();
    var groupRoles: ArrayList<Role> = arrayListOf();
    var groupCodes: ArrayList<String> = arrayListOf();

    var restLoaded: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null)
            uid = auth.currentUser!!.uid
        else {
            val intent = Intent(this, LoginActivity::class.java);
            startActivity(intent);
            finish();
        }
        db = FirebaseFirestore.getInstance()
        loadDB()
    }

    private fun loadRest() {
        if (!restLoaded) {
            restLoaded = true
            setContentView(R.layout.activity_main)
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_shifts,
                    R.id.navigation_next,
                    R.id.navigation_group,
                    R.id.navigation_settings
                )
            )
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)
        } else {
            val navView: BottomNavigationView = findViewById(R.id.nav_view)
            navView.selectedItemId = navView.selectedItemId
        }
    }

    fun loadDB() {
        groupEmployees.clear()
        groupShifts.clear()
        groupRoles.clear()
        groupCodes.clear()
        loadGroupCodes()
        loadUser()
    }

    private fun loadGroupCodes() {
        db.collection("groupCodes").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (result in task.result!!) {
                    groupCodes.add(result.id)
                }
                Log.d(TAG, "Group codes loaded")
            } else {
                Log.d(TAG, "Group codes get failed: ", task.exception)
            }
        }
    }

    private fun loadUser() {
        db.collection("users").document(uid).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var data = task.result?.data
                if (!data.isNullOrEmpty()) {
                    user = Employee(
                        data["uid"] as String,
                        data["groupCode"] as String,
                        data["firstName"] as String,
                        data["lastName"] as String,
                        data["manager"] as Boolean,
                        data["owner"] as Boolean
                    )
                    Log.d(TAG, "User loaded")
                    hasGroup = (user.groupCode != "000000")
                }
            } else {
                Log.d(TAG, "user load failed: ", task.exception)
            }
            if (hasGroup)
                loadGroup()
            else
                loadRest()
        }
    }

    private fun loadGroup() {
        db.collection("groups").document(user.groupCode).get().addOnSuccessListener { document ->
            if (document.exists()) {
                Log.d(TAG, "Group document data: ${document.data}")
                group = document.toObject(Group::class.java)!!
                Log.d(TAG, "Group data: ${group.toString()}")
            } else
                Log.d(TAG, "No such document")
            loadEmployees()
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Group get failed with ", exception)
            loadEmployees()
        }
    }

    private fun loadEmployees() {
        db.collection("groups").document(user.groupCode).collection("employees").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    groupEmployees.add(document.toObject(Employee::class.java))
                    Log.d(TAG, "Group employee data: ${groupEmployees.toString()}")
                }
                loadRoles()
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Group employees get failed with ", exception)
                loadRoles()
            }
    }

    private fun loadRoles() {
        db.collection("groups").document(user.groupCode).collection("roles").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    groupRoles.add(document.toObject(Role::class.java))
                    Log.d(TAG, "Group roles data: ${groupRoles.toString()}")
                }
                loadShifts()
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Role get failed with ", exception)
                loadShifts()
            }
    }

    private fun loadShifts() {
        db.collection("groups").document(user.groupCode).collection("shifts").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    groupShifts.add(document.toObject(Shift::class.java))
                    Log.d(TAG, "Group shift data: ${groupShifts.toString()}")
                }
                loadRest()
            }.addOnFailureListener { exception ->
                Log.d(TAG, "Shift get failed with ", exception)
                loadRest()
            }
    }

    fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onFinishEditDialog(
        rid: String,
        name: String,
        startTime: String,
        endTime: String,
        color: Int
    ) {
        val role: Role = Role(rid, name, startTime, endTime, color)
        Log.d(TAG, "Role to add: $role")
        //increment roleNumber
        //add to roles
        if (rid.isEmpty())
            incrementRoleNumber(role)
        else //save role
            addRole(role)
    }

    private fun incrementRoleNumber(role: Role) {
        db.collection("groups").document(user.groupCode).get().addOnSuccessListener { document ->
            if (document.exists()) {
                Log.d(TAG, "Group document data: ${document.data}")
                group = document.toObject(Group::class.java)!!
                Log.d(TAG, "Group data: $group")
                db.collection("groups").document(user.groupCode)
                    .update("roleNumber", group.roleNumber + 1)
                role.rid = group.roleNumber.toString()
                addRole(role)
            } else
                Log.d(TAG, "No such document")
        }.addOnFailureListener { exception ->
            Log.d(TAG, "Increment role failed with ", exception)
        }
    }

    private fun addRole(role: Role) {
        db.collection("groups").document(user.groupCode).collection("roles")
            .document(role.rid)
            .set(role)
            .addOnSuccessListener {
                Log.d(TAG, "Role successfully written!")
                loadDB()
                Toast.makeText(this, "Role saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing role", e)
                loadDB()
            }
    }

    override fun onFinishEditDialog(shift: Shift) {
        Log.d(TAG, "Shift to add: $shift")
        //add shift
        addShift(shift)
    }

    private fun addShift(shift: Shift) {
        db.collection("groups").document(user.groupCode).collection("shifts").add(shift)
            .addOnSuccessListener {
                Log.d(TAG, "Shift successfully written!")
                loadDB()
                Toast.makeText(this, "Shift added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing shift", e)
                loadDB()
            }
    }
}
