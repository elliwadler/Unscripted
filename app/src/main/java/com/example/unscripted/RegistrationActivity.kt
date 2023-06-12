package com.example.unscripted

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class RegistrationActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val tvLogin : TextView = findViewById(R.id.tv_register_login)
        tvLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }

        setupActionBar()
    }

    private fun setupActionBar(){
        val toolbarRegistrationActivity : Toolbar = findViewById(R.id.toolbar_register_activity)
        setSupportActionBar(toolbarRegistrationActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            //This will make own action clickable and the "<-" at the left side
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back)
        }
        toolbarRegistrationActivity.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }
}