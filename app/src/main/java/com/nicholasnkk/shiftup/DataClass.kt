package com.nicholasnkk.shiftup

import android.graphics.Color

data class Employee(var uid:String = "", var groupCode:String = "", var firstName:String = "", var lastName:String = "", var manager:Boolean = false, var owner:Boolean = false)

data class Role(var rid:String = "", var name:String = "", var startTime: String = "", var endTime: String = "", var color:Int = Color.BLACK)
data class Shift(var uid:String = "", var rid: String = "", var date:String = "")
data class Group(var roleNumber:Int = 0)

//to manage unique codes
data class GroupCode(var groupCode:String = "", var uid:String = "")