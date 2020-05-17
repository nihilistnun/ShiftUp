package com.nicholasnkk.shiftup.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nicholasnkk.shiftup.*

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.qualifiedName

    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    lateinit var uid: String
    lateinit var user: Employee
    var hasGroup: Boolean = false
    lateinit var group: Group
    var groupCodes: ArrayList<String> = arrayListOf();


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
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_shifts,
                R.id.navigation_next,
                R.id.navigation_employees,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun loadDB() {
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
                Log.d(TAG, "Cached get failed: ", task.exception)
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
                    if (user.groupCode != "000000") {
                        hasGroup = true;
                        loadGroup()
                    }
                }
            } else {
                Log.d(TAG, "user load failed: ", task.exception)
            }
        }
    }

    private fun loadGroup() {
        db.collection("groups").document(user.groupCode).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var doc: DocumentSnapshot? = task.result
                if (doc != null) {
                    group = doc.toObject(Group::class.java)!!
                }
                Log.d(TAG, "Group : $group")
//                var data = task.result?.data
//                if (!data.isNullOrEmpty()) {
//                    var employeeList: ArrayList<Employee> = arrayListOf()
//                    var roleList: ArrayList<Role> = arrayListOf()
//                    var rotaList: ArrayList<Rota> = arrayListOf()
//                    group = Group(
//                        data["groupCode"] as String,
//                        data["uid"] as String,
//                        data["employeeList"] as java.util.ArrayList<Employee>,
//                        data["roleList"] as java.util.ArrayList<Role>,
//                        data["rotaList"] as java.util.ArrayList<Rota>
//                    )
//                }
                Log.d(TAG, "Group loaded")
                loadRest()
            } else {
                Log.d(TAG, "user load failed: ", task.exception)
            }
        }
    }
}
