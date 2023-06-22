// Basis functionalities
// last updated 02.06.2023
// Author Elisabeth Wadler
package com.example.unscripted

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
        } else {
            snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.green))
        }

        snackbar.show()
    }
}