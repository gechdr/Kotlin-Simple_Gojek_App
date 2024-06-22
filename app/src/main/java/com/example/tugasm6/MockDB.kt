package com.example.tugasm6

class MockDB {
    companion object {
        // User data
        val listUser:MutableList<User> = mutableListOf(
            User("morgan", "123", "Christian Morgan", "08123456789", 1000000, "Customer"),
            User("medon", "123", "Charles Medon", "081234567890", 2000000, "Driver"),
            User("martin", "123", "Cole Martin", "081123456789", 3000000, "Customer"),
            User("randy", "123", "Randy Arias", "083898765432", 4000000, "Customer"),
            User("esme", "123", "Esme Watts", "085765432109", 5000000, "Driver"),
            User("andy", "123", "Andy Tran", "085232109876", 6000000, "Driver")
        )

        // Order data
        val listOrder:MutableList<Order> = mutableListOf(
            Order(1, "Bike", "morgan", "medon", "Surabaya", "Jakarta", 195000, "On Going"),
            Order(2, "Car", "morgan", "esme", "Surabaya", "Malang", 79000, "On Going"),
            Order(3, "Bike", "morgan", "", "Surabaya", "Aceh", 273000, "Waiting"),
            Order(4, "Bike", "morgan", "andy", "Surabaya", "Jakarta", 195000, "Done"),
        )
    }
}