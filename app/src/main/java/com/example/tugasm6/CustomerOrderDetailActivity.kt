package com.example.tugasm6

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tugasm6.CurrencyUtils.toRupiah
import com.example.tugasm6.databinding.ActivityCustomerOrderDetailBinding

class CustomerOrderDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityCustomerOrderDetailBinding

    private val orderViewModel: OrderViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_order_detail)

        // Initialization
        val orderID = intent.getStringExtra("orderID")?.toInt()
        if (orderID != null) {
            orderViewModel.activeOrder(orderID)
        } else {
            Toast.makeText(this, "orderID null", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.CODAtvType.text = orderViewModel.getOrderType()
        binding.CODAtvPickUpValue.text = orderViewModel.getOrderPickUp()
        binding.CODAtvDestinationValue.text = orderViewModel.getOrderDestination()
        binding.CODAtvFareValue.text = orderViewModel.getOrderFare()?.toRupiah()

        // Driver
        var driver: User? = null
        driver = userViewModel.getUserByUsername(orderViewModel.getOrderDriver()!!)

        // Status
        val status = orderViewModel.getOrderStatus()!!
        if (status == "Waiting") {
            binding.CODAbtnSMS.backgroundTintList = getColorStateList(R.color.gray)
        } else {
            binding.CODAbtnSMS.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:${driver?.phoneNumber}")
                    putExtra("sms_body", "")
                }
                startActivity(intent)
            }
        }
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