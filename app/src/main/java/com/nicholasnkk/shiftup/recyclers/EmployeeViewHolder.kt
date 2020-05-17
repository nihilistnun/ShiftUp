package com.nicholasnkk.shiftup.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.Employee
import com.nicholasnkk.shiftup.R

class EmployeeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.employee_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null


    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
    }

    fun bind(employee: Employee) {
        val name: String = employee.firstName + " " + employee.lastName
        mTitleView?.text = name
        var position: String = "Employee";
        if (employee.owner)
            position = "Owner"
        else if (employee.manager)
            position = "Manager"
        mYearView?.text = position
    }

}
