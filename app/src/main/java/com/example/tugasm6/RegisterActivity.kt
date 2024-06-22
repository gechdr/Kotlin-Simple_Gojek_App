package com.example.tugasm6

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import com.example.tugasm6.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.RAbtnRegister.setOnClickListener {
            val username = binding.RAetUsername.text.toString()
            val name = binding.RAetName.text.toString()
            val password = binding.RAetPassword.text.toString()
            val confirmPassword = binding.RAetConfirmPassword.text.toString()
            val phoneNumber = binding.RAetPhoneNumber.text.toString()
            var role = ""

            // Check Username
            val user = userViewModel.getUserByUsername(username)

            // Fill Role
            if (binding.RArbMitra.isChecked) {
                role = "Driver"
            } else if (binding.RArbCustomer.isChecked) {
                role = "Customer"
            }

            if (username.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phoneNumber.isEmpty()) {
                // Checking if the fields are empty
                binding.RAetUsername.error = "Please fill this field"
                binding.RAetName.error = "Please fill this field"
                binding.RAetPassword.error = "Please fill this field"
                binding.RAetConfirmPassword.error = "Please fill this field"
                binding.RAetPhoneNumber.error = "Please fill this field"
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()

            } else if (user != null) {
                // Checking if the username is already taken
                binding.RAetUsername.error = "Username already taken"
                Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show()

            } else if (password != confirmPassword) {
                // Checking if the password and confirm password are the same
                binding.RAetConfirmPassword.error = "Password doesn't match"
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()

            } else if (phoneNumber.length != 12 || !phoneNumber.isDigitsOnly()) {
                // Checking if the phone number is valid
                binding.RAetPhoneNumber.error = "Phone number must be 12 characters of numbers"
                Toast.makeText(this, "Phone number must be 12 characters of numbers", Toast.LENGTH_SHORT).show()

            } else {
                // Adding the user to the database
                val tempUser = (User(username, password, name, phoneNumber, 0, role))
                userViewModel.addUser(tempUser)
                Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                println(tempUser)

                // Reset Input
                binding.RAetUsername.text.clear()
                binding.RAetName.text.clear()
                binding.RAetPassword.text.clear()
                binding.RAetConfirmPassword.text.clear()
                binding.RAetPhoneNumber.text.clear()
                binding.RArbMitra.isChecked = true
                binding.RArbCustomer.isChecked = false

                // Navigate
                finish()
            }
        }

        binding.RAbtnLogin.setOnClickListener {

            binding.RAetUsername.text.clear()
            binding.RAetName.text.clear()
            binding.RAetPassword.text.clear()
            binding.RAetConfirmPassword.text.clear()
            binding.RAetPhoneNumber.text.clear()
            binding.RArbMitra.isChecked = true
            binding.RArbCustomer.isChecked = false

            finish()
        }
    }
}