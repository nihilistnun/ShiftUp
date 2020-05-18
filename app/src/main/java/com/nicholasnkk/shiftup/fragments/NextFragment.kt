package com.nicholasnkk.shiftup.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.ShiftDialog
import com.nicholasnkk.shiftup.activities.MainActivity
import com.nicholasnkk.shiftup.recyclers.RoleListAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NextFragment : Fragment() {
    private val TAG = NextFragment::class.qualifiedName
    private lateinit var main: MainActivity

    private lateinit var addShiftBtn: Button
    private lateinit var deleteShiftBtn: Button

    private lateinit var monTV: TextView
    private lateinit var tueTV: TextView
    private lateinit var wedTV: TextView
    private lateinit var thuTV: TextView
    private lateinit var friTV: TextView
    private lateinit var satTV: TextView
    private lateinit var sunTV: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main = activity as MainActivity
        val root = inflater.inflate(R.layout.fragment_next, container, false)

        addShiftBtn = root.findViewById(R.id.addShift)
        deleteShiftBtn = root.findViewById(R.id.deleteShift)

        monTV = root.findViewById(R.id.monday_employees)
        tueTV = root.findViewById(R.id.tuesday_employees)
        wedTV = root.findViewById(R.id.wednesday_employees)
        thuTV = root.findViewById(R.id.thursday_employees)
        friTV = root.findViewById(R.id.friday_employees)
        satTV = root.findViewById(R.id.saturday_employees)
        sunTV = root.findViewById(R.id.sunday_employees)

        updateTextViews()

        addShiftBtn.setOnClickListener {
            //add shift dialog
            val dialogFragment = ShiftDialog()
            dialogFragment.show(main.supportFragmentManager, "dialog")
        }

        deleteShiftBtn.setOnClickListener {
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

    @SuppressLint("SimpleDateFormat")
    private fun updateTextViews() {
        val textViews: Array<String> = arrayOf("", "", "", "", "", "", "")
        var textColors = arrayOf<ArrayList<Int>>()
        for (i in (0..6)) {
            textColors += ArrayList<Int>()
        }
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, 7)
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        for (shift in main.groupShifts) {
            for (day in (0..6)) {
                cal.add(Calendar.DAY_OF_YEAR, day)
                val date: String = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
                if (shift.date == date) {
                    //display in the table
                    textViews[day] += getEmployeeName(shift.uid) + ", "
                    textColors[day].add(getColor(shift.rid))
                }
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
            }
        }
        for (i in (0..6)) {
            if (textViews[i].isEmpty())
                textViews[i] = "None"
            else
                textViews[i] = textViews[i].substring(0, textViews[i].length - 2)
        }
        monTV.text = getSpannableString(textViews[0], textColors[0])
        tueTV.text = getSpannableString(textViews[1], textColors[1])
        wedTV.text = getSpannableString(textViews[2], textColors[2])
        thuTV.text = getSpannableString(textViews[3], textColors[3])
        friTV.text = getSpannableString(textViews[4], textColors[4])
        satTV.text = getSpannableString(textViews[5], textColors[5])
        sunTV.text = getSpannableString(textViews[6], textColors[6])
    }

    private fun getEmployeeName(uid: String): String {
        for (employee in main.groupEmployees) {
            if (employee.uid == uid)
                return employee.firstName + " " + employee.lastName
        }
        return "None"
    }

    private fun getColor(rid: String): Int {
        for (role in main.groupRoles) {
            if (role.rid == rid)
                return role.color
        }
        return Color.BLACK
    }

    private fun getSpannableString(text: String, colors: ArrayList<Int>): SpannableString {
        val result:SpannableString = SpannableString(text)
        var index: Int = 0
        var endIndex: Int = 0
        for (color in colors) {
            endIndex = text.indexOf(',', index)
            if (endIndex<0)
                endIndex = text.length
            result.setSpan(BackgroundColorSpan(color), index, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            index = endIndex+2
        }
        return result
    }
}
