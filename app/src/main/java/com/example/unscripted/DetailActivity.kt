package com.example.unscripted

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonElement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val gson = Gson()
        val selectedItemJson = intent.getStringExtra("selectedItem")
        val selectedItem = gson.fromJson(selectedItemJson, Entry::class.java)

            // Display the details in the layout
            val dateTextView: TextView = findViewById(R.id.text_date_entry)
            val stringTextView: TextView = findViewById(R.id.text_title_entry)
            dateTextView.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedItem.date)
            stringTextView.text = selectedItem.title


        }
}
