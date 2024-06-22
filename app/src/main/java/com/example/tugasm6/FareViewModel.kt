package com.example.tugasm6

import androidx.lifecycle.ViewModel

class FareViewModel: ViewModel() {
    private var fare:Int = 0
    var multiplier:Int = 1000

    fun calculateFare(){
        val rnd = (1..1000).random()
        fare = rnd * multiplier
    }

    fun getFare(): Int {
        return fare
    }
}