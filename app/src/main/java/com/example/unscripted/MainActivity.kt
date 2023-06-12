package com.example.unscripted

import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalender()
        setupListView()
        setupBottomNaviagtion()
    }
    override fun onStart() {
        super.onStart()

        val greetingText: TextView = findViewById(R.id.text_greeting)
        greetingText.text = getGreetingBasedOnTime()
    }


    fun setupListView(){
        val e = Entry()
        e.title = "First Entry"
        e.date = Date()
        e.text = "blabla"
        e.image = "image1"

        val e2 = Entry()
        e2.title = "Second Entry"
        e2.date = Date()
        e2.text = "blabla"
        e2.image = "image2"
        val dataList = listOf(
            e,
            e2
            // Add more entries as needed
        )

        var listView: ListView = findViewById(R.id.list_recent_entries)

        val height = 120 * dataList.size
        val height_px= (height * resources.displayMetrics.density).toInt()

        val layoutParams = listView.layoutParams
        layoutParams.height = height_px
        listView.layoutParams = layoutParams

        val adapter = EntryAdapter(this, dataList)
        listView.adapter = adapter

        val gson = Gson()

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = dataList[position]
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("selectedItem", gson.toJson(selectedItem))
            startActivity(intent)
        }
    }

    fun setupBottomNaviagtion(){
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED
        var selectedItem: MenuItem? = null

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            selectedItem = item
            val intent = Intent(this, NewEntryActivity::class.java)
            startActivity(intent)
            true
        }
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



