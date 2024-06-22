package com.example.tugasm6

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderViewModel : ViewModel() {

    val order: MutableLiveData<Order?> = MutableLiveData(null)
    val _order: Order?
        get() = order.value

    var orders: MutableLiveData<MutableList<Order>> = MutableLiveData(MockDB.listOrder)
    val _orders: MutableList<Order>
        get() = orders.value!!

    // Data Logic
    fun addOrder(o: Order) {
        MockDB.listOrder.add(o)
    }

    fun setOrders(listOrder: MutableList<Order>) {
        orders.value = listOrder
    }

    fun getOrderByID(id: Int): Order? {
        val temp: Order? = MockDB.listOrder.find { it.id == id }
        return temp
    }

    fun getOrderByCustomer(customer: String): MutableList<Order> {
        return MockDB.listOrder.filter { it.customer == customer && it.status != "Done" }.toMutableList()
    }

    fun activeOrder(id: Int){
        if (id == -1) {
            order.value = null
        } else {
            order.value = getOrderByID(id)
        }
    }

    fun getOrderType(): String? {
        return order.value?.type
    }

    fun getOrderPickUp(): String? {
        return order.value?.pickUp
    }

    fun getOrderDestination(): String? {
        return order.value?.destination
    }

    fun getOrderFare(): Int? {
        return order.value?.fare
    }

    fun getOrderDriver(): String? {
        if (order.value?.driver == "") {
            return ""
        }
        return order.value?.driver
    }
    fun getOrderCustomer(): String? {
        return order.value?.customer
    }

    fun getOrderStatus(): String? {
        return order.value?.status
    }

    fun getOrderByDriver(driver: String): Order? {
        return MockDB.listOrder.find { it.driver == driver }
    }

    fun getOrderByStatus(status: String): MutableList<Order> {
        return MockDB.listOrder.filter { it.status == status }.toMutableList()
    }

    fun finishOrder(id: Int) {
        MockDB.listOrder.find { it.id == id }?.let {
            it.status = "Done"
        }
    }

    fun acceptOrder(id: Int, driver: String) {
        MockDB.listOrder.find { it.id == id }?.let {
            it.status = "On Going"
            it.driver = driver
        }
    }

    fun getNewID(): Int {
        val lastID = MockDB.listOrder.last().id
        return lastID + 1
    }
}