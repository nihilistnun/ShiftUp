package com.nicholasnkk.shiftup.recyclers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.Role

class RoleListAdapter(private val list:  ArrayList<Role>)
    : RecyclerView.Adapter<RoleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RoleViewHolder(
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val role: Role = list[position]
        holder.bind(role)
    }

    override fun getItemCount(): Int = list.size

}