package com.nicholasnkk.shiftup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.RoleDialog
import com.nicholasnkk.shiftup.ShiftDialog
import com.nicholasnkk.shiftup.activities.MainActivity
import com.nicholasnkk.shiftup.recyclers.RoleListAdapter

class NextFragment : Fragment() {
    private val TAG = NextFragment::class.qualifiedName
    private lateinit var main: MainActivity

    private lateinit var addShiftBtn: Button
    private lateinit var deleteShiftBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = activity as MainActivity
        val root = inflater.inflate(R.layout.fragment_next, container, false)

        addShiftBtn = root.findViewById(R.id.addShift)
        deleteShiftBtn = root.findViewById(R.id.deleteShift)

        addShiftBtn.setOnClickListener{
            //add shift dialog
            val dialogFragment = ShiftDialog()
            dialogFragment.show(main.supportFragmentManager, "dialog")
        }

        deleteShiftBtn.setOnClickListener{
            //add shift dialog
            val dialogFragment = ShiftDialog()
            dialogFragment.toogleDelete()
            dialogFragment.show(main.supportFragmentManager, "dialog")
        }
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
