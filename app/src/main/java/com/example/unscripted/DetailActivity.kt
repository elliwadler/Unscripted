package com.example.unscripted

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val gson = Gson()
        gson.fromJson(intent.getStringExtra("selectedItem"), )
        val selectedItem = intent.getSerializableExtra("selectedItem") as Pair<Date, String>

        // Display the details in the layout
        val dateTextView: TextView = findViewById(R.id.text_date_entry)
        val stringTextView: TextView = findViewById(R.id.text_title_entry)
        dateTextView.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedItem.first)
        stringTextView.text = selectedItem.second


        val myImage: ImageView = findViewById(R.id.image_back)

        myImage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}