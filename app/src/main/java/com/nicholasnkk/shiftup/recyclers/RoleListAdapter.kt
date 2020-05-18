package com.nicholasnkk.shiftup.recyclers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.Role
import com.nicholasnkk.shiftup.RoleDialog
import com.nicholasnkk.shiftup.activities.MainActivity

class RoleListAdapter(private val context: Context, private val list:  ArrayList<Role>): RecyclerView.Adapter<RoleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RoleViewHolder(inflater,parent).listen{ pos, _ ->
            val item = list[pos]
            val dialogFragment = RoleDialog()
            dialogFragment.populate(item)
            dialogFragment.show((context as MainActivity).supportFragmentManager, "dialog")
        }
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val role: Role = list[position]
        holder.bind(role)
    }

    override fun getItemCount(): Int = list.size

}