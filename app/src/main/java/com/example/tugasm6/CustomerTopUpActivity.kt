package com.example.tugasm6

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tugasm6.databinding.ActivityCustomerTopUpBinding

class CustomerTopUpActivity : AppCompatActivity() {

    lateinit var binding: ActivityCustomerTopUpBinding

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_top_up)

        val username = intent.getStringExtra("user")
        userViewModel.login(username!!)

        binding.CTAbtnTopUp.setOnClickListener {
            val amount = binding.CTAetNominal.text.toString().toInt()

            if (amount < 1000) {
                binding.CTAetNominal.error = "Minimal top up is Rp 1.000"
                Toast.makeText(this, "Minimal top up is Rp 1.000", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                // Top Up
                userViewModel.increaseBalance(username, amount)

                println(MockDB.listUser.find { it.username == username }!!.balance)

                Toast.makeText(this, "Top up success", Toast.LENGTH_SHORT).show()
                binding.CTAetNominal.text.clear()
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