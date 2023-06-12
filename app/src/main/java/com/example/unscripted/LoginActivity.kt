package com.example.unscripted

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.unscripted.R.layout.activity_login)

        val tv_register = findViewById<TextView>(com.example.unscripted.R.id.tv_login_register)
        tv_register.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        val tv_forgot_pw = findViewById<TextView>(com.example.unscripted.R.id.tv_login_forgotpassword)
        tv_forgot_pw.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        val btn_login = findViewById<Button>(com.example.unscripted.R.id.btn_login_login)
        btn_login.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}