package com.example.unscripted

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : BasisActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister = findViewById<TextView>(R.id.tv_login_register)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        val tvForgotPw = findViewById<TextView>(R.id.tv_login_forgotpassword)
        tvForgotPw.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        val btnLogin = findViewById<Button>(R.id.btn_login_login)
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser(){
        val emailID : String = (findViewById<EditText>(R.id.et_login_email)).text.toString().trim{ it <= ' '}
        val password : String = (findViewById<EditText>(R.id.et_login_password)).text.toString().trim{ it <= ' '}

        if(validateLoginInformation(emailID, password)){
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(emailID, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        CloudFirestore().getUserDetails(this)
                    } else {
                        showCustomSnackbar(task.exception!!.message.toString(), true);
                    }
                }
        }
    }

    private fun validateLoginInformation(emailID:String, password:String) : Boolean{
        return when {
            TextUtils.isEmpty(emailID) -> {
                (findViewById<TextInputLayout>(R.id.til_login_email)).error = getString(R.string.errormessage_login_email)
                false
            }

            TextUtils.isEmpty(password) -> {
                (findViewById<TextInputLayout>(R.id.til_login_password)).error = getString(R.string.errormessage_login_password)
                false
            }

            else -> true
        }
    }

    fun userLoggedInSuccess(user : User){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}