package com.nicholasnkk.shiftup.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.activities.MainActivity
import com.nicholasnkk.shiftup.recyclers.EmployeeListAdapter
import com.nicholasnkk.shiftup.recyclers.RoleListAdapter

class NextFragment : Fragment() {
    private val TAG = NextFragment::class.qualifiedName
    private lateinit var main: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_next, container, false)
        main = activity as MainActivity
        Log.d(TAG, main.groupRoles.size.toString())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // RecyclerView node initialized here
        if (main.hasGroup) {
            val roleRecycler: RecyclerView = view.findViewById(R.id.role_recycler_view)
            roleRecycler.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = RoleListAdapter(main, main.groupRoles)
            }
        }
    }
}
