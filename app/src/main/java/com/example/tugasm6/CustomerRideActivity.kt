package com.example.tugasm6

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tugasm6.CurrencyUtils.toRupiah
import com.example.tugasm6.databinding.ActivityCustomerRideBinding

class CustomerRideActivity : AppCompatActivity() {

    lateinit var binding: ActivityCustomerRideBinding

    private val fareViewModel: FareViewModel by viewModels()
    private val orderViewModel: OrderViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_ride)

        val user = intent.getStringExtra("user")
        val type = intent.getStringExtra("type")

        // Set User
        if (user != null) {
            userViewModel.login(user)
        }

        // Set Value
        binding.CRAtvType.text = type

        // Calculate Fare
        binding.CRAetPickUp.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.CRAetDestination.text.isNotEmpty()) {
                fareViewModel.calculateFare()
                binding.CRAtvFareValue.text = fareViewModel.getFare().toRupiah()
                v.hideKeyboard()
            }
        }
        binding.CRAetDestination.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.CRAetPickUp.text.isNotEmpty()) {
                fareViewModel.calculateFare()
                binding.CRAtvFareValue.text = fareViewModel.getFare().toRupiah()
                v.hideKeyboard()
            }
        }

        // Enter To Move
        binding.CRAetPickUp.setOnEditorActionListener { v, actionId, event ->
            if (actionId == 6 && binding.CRAetDestination.text.isNotEmpty()) {
                fareViewModel.calculateFare()
                binding.CRAtvFareValue.text = fareViewModel.getFare().toRupiah()
                v.hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.CRAetDestination.setOnEditorActionListener { v, actionId, event ->
            println(actionId)
            if (actionId == 6 && binding.CRAetPickUp.text.isNotEmpty()) {
                fareViewModel.calculateFare()
                binding.CRAtvFareValue.text = fareViewModel.getFare().toRupiah()
                v.hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        // Action
        if (user != null && type != null){
            binding.CRAbtnOrder.setOnClickListener {
                if (fareViewModel.getFare() > 0) {
                    val pickUp = binding.CRAetPickUp.text.toString()
                    val destination = binding.CRAetDestination.text.toString()

                    // Check
                    if (pickUp.isEmpty() || destination.isEmpty()){
                        binding.CRAetPickUp.error = "Please fill this field!"
                        binding.CRAetDestination.error = "Please fill this field!"
                        Toast.makeText(this, "Please fill all field!", Toast.LENGTH_SHORT).show()
                    }else if (checkBalance()){
                        // Order
                        val newOrder:Order = Order(
                            orderViewModel.getNewID(),
                            type!!,
                            user!!,
                            "",
                            pickUp,
                            destination,
                            fareViewModel.getFare(),
                            "Waiting"
                        )
                        // Save Order
                        orderViewModel.addOrder(newOrder)
                        // Redirect to Customer Home
                        finish()
                    }
                } else {
                    fareViewModel.calculateFare()
                    binding.CRAtvFareValue.text = fareViewModel.getFare().toRupiah()
                }
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

    fun checkBalance(): Boolean {
        if (fareViewModel.getFare() > userViewModel.getBalance()){
            Toast.makeText(this, "Insufficient Balance!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

