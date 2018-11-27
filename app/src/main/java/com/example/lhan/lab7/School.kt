package com.example.lhan.lab7

data class School(
    val name : String? = "",
    val city : String? = "",
    val state : String? = "",
    val zip : String? = "",
    val latitude : Double?,
    val longitude : Double?
)

data class SchoolList (val schools : List<School>)