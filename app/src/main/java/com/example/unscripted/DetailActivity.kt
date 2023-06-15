package com.example.unscripted

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupActionBar()

        val gson = Gson()
        val selectedItemJson = intent.getStringExtra("selectedItem")
        val selectedItem = gson.fromJson(selectedItemJson, Entry::class.java)

            // Display the details in the layout
            val dateTextView: TextView = findViewById(R.id.text_date_entry)
            val stringTextView: TextView = findViewById(R.id.text_title_entry)
            dateTextView.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedItem.date)
            stringTextView.text = selectedItem.title


        }
    private fun setupActionBar(){
        val toolbarRegistrationActivity : Toolbar = findViewById(R.id.toolbar_detail_activity)
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
