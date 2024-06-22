package com.example.tugasm6

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugasm6.CurrencyUtils.toRupiah

class UserViewModel: ViewModel() {

    var user:MutableLiveData<User> = MutableLiveData(null)

    // Data Logic
    fun addUser(u: User) {
        MockDB.listUser.add(u)
    }

    fun getUserByUsername(username: String): User? {
        return MockDB.listUser.find { it.username == username }
    }

    fun getUser(): User? {
        return user.value
    }

    fun getBalance(): Int {
        return user.value!!.balance
    }

    fun increaseBalance(username: String, amount: Int) {
        MockDB.listUser.find { it.username == username }!!.balance += amount
//        user.value!!.balance += amount
    }
    fun decreaseBalance(username: String, amount: Int) {
        MockDB.listUser.find { it.username == username }!!.balance -= amount
//        user.value!!.balance -= amount
    }

    // Logic
    fun login(username: String) {
        val user = getUserByUsername(username)
        this.user.value = user
    }
}