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
import com.karabo.vibe.network.dto.LoginRequest
import com.karabotshivhase.vibe.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tokenManager = TokenManager(this)

        // Check if user is already logged in
        if (!tokenManager.getToken().isNullOrEmpty()) {
            navigateToHome()
            return
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegisterRedirect = findViewById<TextView>(R.id.tvRegisterRedirect)

        btnLogin.setOnClickListener {
            performLogin()
        }

        tvRegisterRedirect.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun performLogin() {
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val loginProgressBar = findViewById<ProgressBar>(R.id.loginProgressBar)

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_SHORT).show()
            return
        }

        loginProgressBar.visibility = View.VISIBLE
        btnLogin.isEnabled = false

        lifecycleScope.launch {
            try {
                val request = LoginRequest(email = email, password = password)
                val response = RetrofitClient.api.login(request)
                
                tokenManager.saveToken(response.token)
                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                navigateToHome()
            } catch (e: Exception) {
                loginProgressBar.visibility = View.GONE
                btnLogin.isEnabled = true
                Toast.makeText(this@LoginActivity, "Authentication failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
