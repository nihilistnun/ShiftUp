package com.nicholasnkk.shiftup

import java.util.*
import kotlin.collections.ArrayList

data class Employee(var uid:String, var groupCode:String, var firstName:String, var lastName:String, var manager:Boolean, var owner:Boolean)

data class Time(var hour:Int, var minute:Int)
data class Role(var name:String, var startTime: Time, var endTime: Time, var color:String)
data class Shift(var uid:String, var role: Role, var date:Date)
data class Rota(var shiftList:ArrayList<Shift>)

data class Group(var groupCode:String, var uid:String, var employeeList:ArrayList<Employee>, var roleList:ArrayList<Role>, var rotaList:ArrayList<Rota>)

//to manage unique codes
data class GroupCode(var groupCode:String, var uid:String)