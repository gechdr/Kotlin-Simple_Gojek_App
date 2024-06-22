package com.example.tugasm6

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasm6.CurrencyUtils.toRupiah
import com.example.tugasm6.databinding.ActivityDriverHomeBinding

class DriverHomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityDriverHomeBinding

    lateinit var orderAdapter: OrderAdapter
    lateinit var layoutManager: LinearLayoutManager

    private val userViewModel: UserViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()

    lateinit var orders: MutableList<Order>
    var username: String? = null
    var order:Order? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_home)

        username = intent.getStringExtra("username")
        userViewModel.login(username!!)

        // Initialize
        binding.DHAtvWelcome.text = "Welcome, ${userViewModel.getUser()!!.name}"
        binding.DHAtvBalanceValue.text = userViewModel.getBalance().toRupiah()

        // Active Order
        order = orderViewModel.getOrderByDriver(username!!)
        if (order!=null && order!!.status == "On Going"){
            orderViewModel.activeOrder(order!!.id)
            binding.DHAlyOngoing.visibility = View.VISIBLE
            //
            if (orderViewModel.getOrderType() == "Car"){
                binding.DHAivOngoing.setBackgroundResource(R.drawable.car)
                binding.DHAtvOngoingType.text = "Car"
            } else {
                binding.DHAivOngoing.setBackgroundResource(R.drawable.bike)
                binding.DHAtvOngoingType.text = "Bike"
            }
            binding.DHAtvOngoingDestination.text = orderViewModel.getOrderDestination()
            binding.DHAtvOngoingSubtotal.text = orderViewModel.getOrderFare()!!.toRupiah()

            binding.DHAbtnOngoingDetail.setOnClickListener {
                val intent = Intent(this, DriverOrderDetailActivity::class.java)
                intent.putExtra("user", username!!)
                intent.putExtra("orderID", order!!.id.toString())
                startActivity(intent)
            }
        } else {
            binding.DHAlyOngoing.visibility = View.GONE
        }

        orders = orderViewModel.getOrderByStatus("Waiting")
        orderViewModel.setOrders(orders)

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        orderAdapter = OrderAdapter(orderViewModel._orders) { order ->
            val intent = Intent(this, DriverOrderDetailActivity::class.java)
            intent.putExtra("user", username!!)
            intent.putExtra("orderID", order.id.toString())
            startActivity(intent)
        }

        binding.DHArvOrders.adapter = orderAdapterS
        binding.DHArvOrders.layoutManager = layoutManager

        // Observer
        val orderObserver = Observer<MutableList<Order>> {
            orderAdapter.data = it
            orderAdapter.notifyDataSetChanged()
        }
        orderViewModel.orders.observe(this, orderObserver)

        val userObserver = Observer<User> {
            binding.DHAtvBalanceValue.text = it.balance.toRupiah()
        }
        userViewModel.user.observe(this, userObserver)

        val activeOrderObserver = Observer<Order?> {
            order = it
            println("Active Order: $order")
            if (order!=null){
                binding.DHAlyOngoing.visibility = View.VISIBLE

                if (orderViewModel.getOrderType() == "Car"){
                    binding.DHAivOngoing.setBackgroundResource(R.drawable.car)
                    binding.DHAtvOngoingType.text = "Car"
                } else {
                    binding.DHAivOngoing.setBackgroundResource(R.drawable.bike)
                    binding.DHAtvOngoingType.text = "Bike"
                }

                binding.DHAtvOngoingDestination.text = orderViewModel.getOrderDestination()
                binding.DHAtvOngoingSubtotal.text = orderViewModel.getOrderFare()!!.toRupiah()

                binding.DHAbtnOngoingDetail.setOnClickListener {
                    val intent = Intent(this, DriverOrderDetailActivity::class.java)
                    intent.putExtra("user", username!!)
                    intent.putExtra("orderID", order!!.id.toString())
                    startActivity(intent)
                }
            } else {
                binding.DHAlyOngoing.visibility = View.GONE
            }
        }
        orderViewModel.order.observe(this, activeOrderObserver)

        binding.DHAbtnLogout.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        //
        orders = orderViewModel.getOrderByStatus("Waiting")
        orderViewModel.setOrders(orders)
        //
        userViewModel.login(username!!)
        //
        order = orderViewModel.getOrderByDriver(username!!)
        if (order?.status == "On Going"){
            orderViewModel.activeOrder(order!!.id)
        } else {
            orderViewModel.activeOrder(-1)
        }
    }
}