package com.example.unscripted

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val selectedItem = intent.getSerializableExtra("selectedItem") as Pair<Date, String>

        // Display the details in the layout
        val dateTextView: TextView = findViewById(R.id.text_date_entry)
        val stringTextView: TextView = findViewById(R.id.text_title_entry)
        dateTextView.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedItem.first)
        stringTextView.text = selectedItem.second
    }
}