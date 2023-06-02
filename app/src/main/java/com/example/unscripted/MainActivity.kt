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
            Pair(Date(), "Hello"),
            Pair(Date(), "World")
        )

        val recyclerView: RecyclerView = findViewById(R.id.list_recent_entries)
        val adapter = EntryAdapter(dataList as MutableList<Pair<Date, String>>)
        recyclerView.adapter = adapter
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