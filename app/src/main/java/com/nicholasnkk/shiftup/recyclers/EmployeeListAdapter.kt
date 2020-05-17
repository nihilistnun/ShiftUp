package com.nicholasnkk.shiftup.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.Employee

class EmployeeListAdapter(private val list:  ArrayList<Employee>)
    : RecyclerView.Adapter<EmployeeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EmployeeViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employee: Employee = list[position]
        holder.bind(employee)
    }

    override fun getItemCount(): Int = list.size

}