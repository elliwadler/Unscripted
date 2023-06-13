package com.example.unscripted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BasisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basis)
    }

    fun showCustomSnackbar(message : String, errorMessage: Boolean){
        val snackbar : Snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)

        if(errorMessage){
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.reddish_orange))
        } else {
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        }

        snackbar.show()
    }
}