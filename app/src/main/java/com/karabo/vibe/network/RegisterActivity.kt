package com.karabo.vibe.network

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.karabo.vibe.network.auth.TokenManager
import com.karabo.vibe.network.dto.RegisterRequest
import com.karabotshivhase.vibe.R
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tokenManager = TokenManager(this)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLoginRedirect = findViewById<TextView>(R.id.tvLoginRedirect)

        btnRegister.setOnClickListener {
            performRegistration()
        }

        tvLoginRedirect.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun performRegistration() {
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val registerProgressBar = findViewById<ProgressBar>(R.id.registerProgressBar)

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            return
        }

        registerProgressBar.visibility = View.VISIBLE
        btnRegister.isEnabled = false

        lifecycleScope.launch {
            try {
                val request = RegisterRequest(email = email, password = password)
                val response = RetrofitClient.api.register(request)
                
                tokenManager.saveToken(response.token)
                Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                
                val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                registerProgressBar.visibility = View.GONE
                btnRegister.isEnabled = true
                Toast.makeText(this@RegisterActivity, "Registration failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
