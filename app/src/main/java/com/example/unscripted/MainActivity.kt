package com.example.unscripted

import Entry
import EntryAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import java.time.LocalDate
import java.util.Calendar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener


class MainActivity : BasisActivity() {

    private lateinit var dataList: List<Entry>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupCalender()
        setupListView()
        setupFloatingActionButton()
        setupActionBar()

        auth = FirebaseAuth.getInstance()
    }
    private fun setupActionBar(){
        val toolbarRegistrationActivity : Toolbar = findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(toolbarRegistrationActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            //This will make own action clickable and the "<-" at the left side
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.logout)
        }
        toolbarRegistrationActivity.setNavigationOnClickListener{
            auth.signOut()
            navigateToLoginScreen()
        }
    }

    private fun navigateToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val greetingText: TextView = findViewById(R.id.text_greeting)
        greetingText.text = getGreetingBasedOnTime()
    }

    override fun onResume() {
        super.onResume()

        setupListView()
    }

    private fun setupListView() {
        val cloudFirestore = CloudFirestore()
        val currentUserID = cloudFirestore.getCurrentUserID()

        cloudFirestore.getAllEntries(
            onSuccess = { retrievedDataList ->
                val filteredList = retrievedDataList.filter { entry -> entry.userId == currentUserID }
                dataList = filteredList.sortedByDescending { it.date }.take(5) // Take only the first five items

                val listView: ListView = findViewById(R.id.list_recent_entries)

                val height = 120 * dataList.size
                val heightPx = (height * resources.displayMetrics.density).toInt()

                val layoutParams = listView.layoutParams
                layoutParams.height = heightPx
                listView.layoutParams = layoutParams

                val adapter = EntryAdapter(this, dataList)
                listView.adapter = adapter

                val gson = Gson()

                listView.setOnItemClickListener { parent, view, position, id ->
                    val selectedItem: Entry = dataList[position]
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra("selectedItem", selectedItem)
                    startActivity(intent)
                }
            },
            onFailure = { exception ->
                showCustomSnackbar(exception!!.toString(), true)
            }
        )
    }


    private fun setupFloatingActionButton() {
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener { item ->
            val intent = Intent(this, NewEntryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupCalendar() {
        val calendarView: CalendarView = findViewById(R.id.calendar_1)

        val cloudFirestore = CloudFirestore()
        val currentUserID = cloudFirestore.getCurrentUserID()

        // Retrieve all entries for the current user
        cloudFirestore.getAllEntries(
            onSuccess = { retrievedDataList ->
                val entryDates = retrievedDataList.filter { entry -> entry.userId == currentUserID }
                    .map { entry -> entry.date }
                    .distinct()

                // Set the highlighted dates on the calendar view
                for (entryDate in entryDates) {
                    val calendar: Calendar = Calendar.getInstance().apply {
                        timeInMillis = entryDate.time
                    }
                    val highlightedDateInMillis: Long = calendar.timeInMillis
                    calendarView.setDateHighlighted(highlightedDateInMillis, true)
                }
            },
            onFailure = { exception ->
                showCustomSnackbar(exception!!.toString(), true)
            }
        )

        val currentDate = LocalDate.now()
        val currentYear: Int = currentDate.year
        val currentMonth: Int = currentDate.monthValue
        val currentDay: Int = currentDate.dayOfMonth

        val calendar: Calendar = Calendar.getInstance().apply {
            set(currentYear, currentMonth, currentDay)
        }

        val defaultDateInMillis: Long = calendar.timeInMillis
        calendarView.date = defaultDateInMillis
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
