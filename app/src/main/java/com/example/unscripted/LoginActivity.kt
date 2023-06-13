package com.example.unscripted

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class LoginActivity : BasisActivity() {

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
            loginUser()
        }
    }

    fun loginUser(){
        val etMail : EditText = findViewById(com.example.unscripted.R.id.et_login_email)
        val etPassword : EditText = findViewById(com.example.unscripted.R.id.et_login_password)

        val email = etMail.text.toString().trim{ it <= ' '}
        val password = etPassword.text.toString().trim{ it <= ' '}

        if(validateLoginInformation(email, password)){
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener  { task ->
                    if (task.isSuccessful) {
                        showCustomSnackbar("You are logged in", false)

                        CloudFirestore().getUserDetails(this)

                    } else {
                        showCustomSnackbar(task.exception!!.toString(), false)
                    }
                }
        }
    }

    private fun validateLoginInformation(email:String, password:String): Boolean {
        return when {
                email.isEmpty() ->{
                    showCustomSnackbar("Please enter email.", true);
                    false
                }
                password.isEmpty() ->{
                    showCustomSnackbar("Please enter a password.", true);
                    false
                }
            else -> true
        }
    }

    fun userLoggedInSuccess(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}