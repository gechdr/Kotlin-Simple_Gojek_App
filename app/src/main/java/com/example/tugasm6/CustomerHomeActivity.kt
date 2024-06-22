package com.example.tugasm6

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.tugasm6.CurrencyUtils.toRupiah
import com.example.tugasm6.databinding.ActivityCustomerHomeBinding

class CustomerHomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityCustomerHomeBinding

    private val userViewModel: UserViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    lateinit var orderAdapter: OrderAdapter
    lateinit var layoutManager: LayoutManager

    lateinit var orders: MutableList<Order>

    var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_home)

        username = intent.getStringExtra("username")
        userViewModel.login(username!!)

        // Initialize
        binding.CHAtvWelcome.text = "Welcome, ${userViewModel.getUser()!!.name}"
        binding.CHAtvBalanceValue.text = userViewModel.getBalance().toRupiah()

        orders = orderViewModel.getOrderByCustomer(username!!)
        orderViewModel.setOrders(orders)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        orderAdapter = OrderAdapter(orderViewModel._orders) { order ->
            val intent = Intent(this, CustomerOrderDetailActivity::class.java)
            intent.putExtra("orderID", order.id.toString())
            startActivity(intent)
        }

        binding.CHArvOrder.adapter = orderAdapter
        binding.CHArvOrder.layoutManager = layoutManager

        // Observer
        val orderObserver = Observer<MutableList<Order>> {
            orderAdapter.data = it
            orderAdapter.notifyDataSetChanged()
        }
        orderViewModel.orders.observe(this, orderObserver)

        val userObserver = Observer<User> {
            binding.CHAtvBalanceValue.text = it.balance.toRupiah()
        }
        userViewModel.user.observe(this, userObserver)

        // Action
        binding.CHAivCar.setOnClickListener {
            Toast.makeText(this, "Order Car", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CustomerRideActivity::class.java)
            intent.putExtra("user", username)
            intent.putExtra("type", "Car")
            startActivity(intent)
        }

        binding.CHAivBike.setOnClickListener {
            Toast.makeText(this, "Order Bike", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CustomerRideActivity::class.java)
            intent.putExtra("user", username)
            intent.putExtra("type", "Bike")
            startActivity(intent)
        }

        binding.CHAivTopUp.setOnClickListener {
            Toast.makeText(this, "Top Up", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, CustomerTopUpActivity::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }

        binding.CHAbtnLogout.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        orders = orderViewModel.getOrderByCustomer(userViewModel.getUser()!!.username)
        orderViewModel.setOrders(orders)
        userViewModel.login(username!!)
    }
}