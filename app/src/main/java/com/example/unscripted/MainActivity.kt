package com.example.unscripted

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalender()

        val dataList = listOf(
            Pair(Date(), "First Entry"),
            Pair(Date(), "Second Entry"),
            Pair(Date(), "Third Entry"),
            // Add more entries as needed
        )

        var listView: ListView = findViewById(R.id.list_recent_entries)

        val height = 120 * dataList.size
        val height_px= (height * resources.displayMetrics.density).toInt()

        val layoutParams = listView.layoutParams
        layoutParams.height = height_px
        listView.layoutParams = layoutParams

        val adapter = EntryListAdapter(this, dataList)
        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = dataList[position]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("selectedItem", selectedItem)
            startActivity(intent)
        }

    }
    override fun onStart() {
        super.onStart()

        val greetingText: TextView = findViewById(R.id.text_greeting)
        greetingText.text = getGreetingBasedOnTime()
    }

    private fun setupCalender(){
        //setup calender
        val currentDate = LocalDate.now()

        val currentYear: Int = currentDate.year
        val currentMonth: Int = currentDate.monthValue
        val currentDay: Int = currentDate.dayOfMonth


        val calendarView: CalendarView = findViewById(R.id.calendar_1)

        val calendar: Calendar = Calendar.getInstance().apply {
            set(currentYear, currentMonth, currentDay)
        }

        val defaultDateInMillis: Long = calendar.timeInMillis
        calendarView.date = defaultDateInMillis
    }

    fun getGreetingBasedOnTime(): String {
        val calendar = Calendar.getInstance()

        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> getString(R.string.morning_greeting)
            in 12..16 -> getString(R.string.day_greeting)
            in 17..20 -> getString(R.string.evening_greeting)
            else -> getString(R.string.night_greeting)
        }
    }
}