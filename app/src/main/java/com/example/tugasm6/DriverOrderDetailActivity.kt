package com.example.tugasm6

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.tugasm6.CurrencyUtils.toRupiah
import com.example.tugasm6.databinding.ActivityDriverOrderDetailBinding

class DriverOrderDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDriverOrderDetailBinding

    private val orderViewModel: OrderViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_order_detail)

        // Initialization
        val orderID = intent.getStringExtra("orderID")?.toInt()
        if (orderID != null) {
            orderViewModel.activeOrder(orderID)
        } else {
            Toast.makeText(this, "orderID null", Toast.LENGTH_SHORT).show()
            finish()
        }
        val username = intent.getStringExtra("user")

        binding.DODAtvType.text = orderViewModel.getOrderType()
        binding.DODAtvPickUpValue.text = orderViewModel.getOrderPickUp()
        binding.DODAtvDestinationValue.text = orderViewModel.getOrderDestination()
        binding.DODAtvFareValue.text = orderViewModel.getOrderFare()?.toRupiah()

        // Customer
        var customer: User? = null
        customer = userViewModel.getUserByUsername(orderViewModel.getOrderCustomer()!!)

        // Status
        val orderObserver: Observer<Order?> = Observer {
            if (it != null) {
                if (it.status == "On Going") {
                    binding.DODAbtnSMS.visibility = View.VISIBLE
                    binding.DODAbtnSMS.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:${customer?.phoneNumber}")
                            putExtra("sms_body", "")
                        }
                        startActivity(intent)
                    }

                    binding.DODAbtnStatus.text = "Done"
                    binding.DODAbtnStatus.setOnClickListener {
                        if (customer!!.balance < orderViewModel.getOrderFare()!!) {
                            Toast.makeText(this, "Customer balance is not enough", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        // Done Order
                        orderViewModel.finishOrder(orderID!!)

                        // Update Balance
                        val customer = userViewModel.getUserByUsername(orderViewModel.getOrderCustomer()!!)
                        val driver = userViewModel.getUserByUsername(orderViewModel.getOrderDriver()!!)
                        val fare = orderViewModel.getOrderFare()!!

                        userViewModel.decreaseBalance(customer!!.username, fare)
                        userViewModel.increaseBalance(driver!!.username, fare)

                        Toast.makeText(this, "Order Done", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    binding.DODAbtnSMS.visibility = View.GONE

                    binding.DODAbtnStatus.text = "Accept"
                    binding.DODAbtnStatus.setOnClickListener {
                        // Eligible to accept order
                        val tempOrder = orderViewModel.getOrderByDriver(username!!)
                        if (tempOrder != null && tempOrder.status == "On Going") {
                            Toast.makeText(this, "You already have active order", Toast.LENGTH_SHORT).show()
                            return@setOnClickListener
                        }

                        // Accept Order
                        orderViewModel.acceptOrder(orderID!!, username)

                        // Refresh Order
                        orderViewModel.activeOrder(orderID)
                        val status = orderViewModel.getOrderStatus()!!

                        Toast.makeText(this, "Order Accepted", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                finish()
            }
        }
        orderViewModel.order.observe(this, orderObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_back -> {
                finish()
                true
            }
            else -> false
        }
    }
}