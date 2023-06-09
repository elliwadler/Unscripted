// First Activity after Login
// last updated 22.06.2023
// Author Elisabeth Wadler
package com.example.unscripted

import Entry
import EntryAdapter
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar


class MainActivity : BasisActivity() {

    private lateinit var dataList: List<Entry>
    private lateinit var auth: FirebaseAuth
    private lateinit var search: ImageView
    private lateinit var searchDate: Calendar
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        searchDate = Calendar.getInstance()
        progressBar = findViewById(R.id.main_progressBar)

        setupCalendar()
        progressBar.visibility = View.VISIBLE
        setupListView(5)
        setupFloatingActionButton()
        setupActionBar()
        setupSearchView()

        var see_all = findViewById<TextView>(R.id.text_all)
        see_all.setOnClickListener{
            show_all_Entries()
        }
    }

    private fun show_all_Entries() {
        progressBar.visibility = View.VISIBLE
        setupListView(null)
        val listView: ListView = findViewById(R.id.list_recent_entries)
        val adapter = EntryAdapter(this, dataList)
        listView.adapter = adapter
        progressBar.visibility = View.GONE

    }


    private fun setupSearchView() {
        search= findViewById(R.id.main_search)
        search.setOnClickListener{
            performSearch()
        }
    }

    private fun performSearch() {
        val year = searchDate.get(Calendar.YEAR)
        val month = searchDate.get(Calendar.MONTH)
        val day = searchDate.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay)
            searchDate.setTime(java.util.Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            val filteredList = dataList.filter { entry ->
                val entryDate = entry.date!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                entryDate == selectedDate
            }

            // Update your ListView with the filtered list of entries
            val listView: ListView = findViewById(R.id.list_recent_entries)
            val adapter = EntryAdapter(this, filteredList)
            listView.adapter = adapter
        }, year, month, day)

        datePicker.show()

    }


    private fun setupActionBar(){
        val iv_logout:ImageView = findViewById(R.id.iv_logout)

        iv_logout.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    override fun onStart() {
        super.onStart()

        val greetingText: TextView = findViewById(R.id.text_greeting)
        greetingText.text = getGreetingBasedOnTime()
    }

    override fun onResume() {
        super.onResume()

        setupListView(5)
    }

    private fun setupListView(amount:Int?) {
        val cloudFirestore = CloudFirestore()
        val currentUserID = cloudFirestore.getCurrentUserID()

        cloudFirestore.getAllEntries(
            onSuccess = { retrievedDataList ->
                val filteredList = retrievedDataList.filter { entry -> entry.userId == currentUserID }
                dataList = if(amount == null)
                    filteredList.sortedByDescending { it.date }.take(filteredList.size)
                else filteredList.sortedByDescending { it.date }.take(amount)

                val listView: ListView = findViewById(R.id.list_recent_entries)

                val height = 120 * dataList.size
                val heightPx = (height * resources.displayMetrics.density).toInt()

                val layoutParams = listView.layoutParams
                layoutParams.height = heightPx
                listView.layoutParams = layoutParams
                val adapter = EntryAdapter(this, dataList)
                listView.adapter = adapter

                progressBar.visibility = View.GONE

                listView.setOnItemClickListener { _, _, position, _ ->
                    val selectedItem: Entry = dataList[position]
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("selectedItem", selectedItem)
                    startActivity(intent)
                }
            },
            onFailure = { exception ->
                showCustomSnackbar(exception.toString(), true)
            }
        )
    }


    private fun setupFloatingActionButton() {
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            val intent = Intent(this, NewEntryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCalendar() {
        val calendarView: CalendarView = findViewById(R.id.calendar_1)
        val currentDate = LocalDate.now()
        val currentYear: Int = currentDate.year
        val currentMonth: Int = currentDate.monthValue - 1 // Months are zero-based in CalendarView
        val currentDay: Int = currentDate.dayOfMonth

        calendarView.date = Calendar.getInstance().apply {
            set(currentYear, currentMonth, currentDay)
        }.timeInMillis
    }




    private fun getGreetingBasedOnTime(): String {
        val calendar = Calendar.getInstance()

        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> getString(R.string.morning_greeting)
            in 12..16 -> getString(R.string.day_greeting)
            in 17..20 -> getString(R.string.evening_greeting)
            else -> getString(R.string.night_greeting)
        }
    }
}