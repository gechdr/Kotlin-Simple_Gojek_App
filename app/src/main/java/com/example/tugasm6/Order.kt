package com.example.tugasm6

data class Order(
    val id:Int,
    val type: String,
    val customer: String,
    var driver: String,
    val pickUp: String,
    val destination: String,
    val fare: Int,
    var status: String
)