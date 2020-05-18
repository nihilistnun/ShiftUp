package com.nicholasnkk.shiftup.recyclers

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nicholasnkk.shiftup.Employee
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.Role

class RoleViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.role_item, parent, false)) {
    private var mTitleView: TextView? = null
    private var mYearView: TextView? = null


    init {
        mTitleView = itemView.findViewById(R.id.list_title)
        mYearView = itemView.findViewById(R.id.list_description)
    }

    fun listen(event: (position: Int, type: Int) -> Unit): RoleViewHolder {
        itemView.setOnClickListener {
            event.invoke(adapterPosition, itemViewType)
        }
        return this
    }

    @SuppressLint("Range")
    fun bind(role: Role) {
        mTitleView?.text = role.name
        mTitleView?.setTextColor(role.color)
        //parse time to string format later
        val description:String = role.startTime + " - " + role.endTime
        mYearView?.text =  description
        mYearView?.setTextColor(role.color)
    }

}
