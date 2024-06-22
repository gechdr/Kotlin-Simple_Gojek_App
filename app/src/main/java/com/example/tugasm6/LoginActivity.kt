package com.example.tugasm6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.tugasm6.CurrencyUtils.toRupiah
import com.example.tugasm6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // Enter To Move
        binding.LAetUsername.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.LAetPassword.text.isNotEmpty()) {
                v.hideKeyboard()
            }
        }
        binding.LAetPassword.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && binding.LAetUsername.text.isNotEmpty()) {
                v.hideKeyboard()
            }
        }

        binding.LAetUsername.setOnEditorActionListener { v, actionId, event ->
            if (actionId == 6 && binding.LAetPassword.text.isNotEmpty()) {
                v.hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }
        binding.LAetPassword.setOnEditorActionListener { v, actionId, event ->
            println(actionId)
            if (actionId == 6 && binding.LAetUsername.text.isNotEmpty()) {
                v.hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.LAbtnLogin.setOnClickListener {
            val username = binding.LAetUsername.text.toString()
            val password = binding.LAetPassword.text.toString()

            // Check if username and password is empty
            if (username.isEmpty() || password.isEmpty()) {
                binding.LAetUsername.error = "Username is empty"
                binding.LAetPassword.error = "Password is empty"
                Toast.makeText(this, "Username or Password is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if user is exist
            val user = userViewModel.getUserByUsername(username)
            if (user == null) {
                binding.LAetUsername.error = "Username not found"
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if password is correct
            if (user.password != password) {
                binding.LAetPassword.error = "Password is incorrect"
                Toast.makeText(this, "Password is incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Login success
            userViewModel.login(user.username)
            Toast.makeText(this, "Welcome, ${user.name}", Toast.LENGTH_SHORT).show()

            // Clear input
            binding.LAetUsername.text.clear()
            binding.LAetPassword.text.clear()

            // Navigate
            println(user.role)
            if (user.role == "Driver"){
                val intent = Intent(this, DriverHomeActivity::class.java)
                intent.putExtra("username", user.username)
                startActivity(intent)
            } else {
                val intent = Intent(this, CustomerHomeActivity::class.java)
                intent.putExtra("username", user.username)
                startActivity(intent)
            }

        }

        binding.LAbtnRegister.setOnClickListener {
            binding.LAetUsername.text.clear()
            binding.LAetPassword.text.clear()

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

