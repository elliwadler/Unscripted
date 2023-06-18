package com.example.unscripted

import Entry
import android.app.AlertDialog
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale


class DetailActivity : BasisActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setupActionBar()

        displayData()

        val btnDelete: FloatingActionButton = findViewById(R.id.fabDelete)
        btnDelete.setOnClickListener {
            val selectedItem = intent.getParcelableExtra<Entry>("selectedItem")

            val dialog = AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Delete") { _, _ ->
                    // User confirmed deletion, proceed with deleting the entry
                    val firestore = CloudFirestore()
                    firestore.deleteEntryCloudFirestore(this, selectedItem!!)
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }

    }

    private fun displayData() {
        val selectedItem = intent.getParcelableExtra<Entry>("selectedItem")

        val dateTextView: TextView = findViewById(R.id.text_date_entry)
        val stringTitleView: TextView = findViewById(R.id.text_title_entry)
        val stringTextView: TextView = findViewById(R.id.text_text_entry)
        dateTextView.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedItem?.date)
        stringTitleView.text = selectedItem?.title
        stringTextView.text = selectedItem?.text

        val imageViewMood: ImageView = findViewById(R.id.iv_detail_emotion)
        val textViewMood: TextView = findViewById(R.id.tv_detail_emotion)
        val mood = selectedItem?.mood ?: 0
        val imageResId = when (mood) {
            1 -> R.drawable.crying
            2 -> R.drawable.sad
            3 -> R.drawable.neutral
            4 -> R.drawable.smile
            5 -> R.drawable.love
            else -> R.drawable.neutral
        }
        imageViewMood.setImageResource(imageResId)
        val textMood = when (mood) {
            1 -> getString(R.string.crying)
            2 -> getString(R.string.sad)
            3 -> getString(R.string.neutral)
            4 -> getString(R.string.laughing)
            5 -> getString(R.string.love)
            else -> getString(R.string.neutral)
        }
        textViewMood.text= textMood.toString()

        val textViewClock: TextView = findViewById(R.id.tv_detail_clock)
        textViewClock.text = selectedItem?.time

        val imageViewWeather: ImageView = findViewById(R.id.iv_detail_weather)
        val textViewWeather: TextView = findViewById(R.id.tv_detail_weather)
        val weather = selectedItem?.weather ?: 0
        val weatherResId = when (mood) {
            1 -> R.drawable.snow
            2 -> R.drawable.rain
            3 -> R.drawable.cloud
            4 -> R.drawable.cloud_sun
            5 -> R.drawable.sun
            else -> R.drawable.cloud_sun
        }
        imageViewWeather.setImageResource(weatherResId)
        val textWeather = when (mood) {
            1 -> getString(R.string.snow)
            2 -> getString(R.string.rain)
            3 -> getString(R.string.Cloud)
            4 -> getString(R.string.cloud_sun)
            5 -> getString(R.string.sun)
            else -> getString(R.string.cloud_sun)
        }
        textViewWeather.text= textWeather.toString()

        val llPhotos: LinearLayout = findViewById(R.id.ll_photos)
        val imageUris: List<String> = selectedItem?.imagePaths ?: emptyList()

        // Iterate through the image URIs and create ImageView instances dynamically
        for (imageUriString in imageUris) {
            val linearLayout: LinearLayout = findViewById(R.id.ll_photos)

            val imageView = ImageView(this)
            val sizeInPx = 100.dpToPx()

            val layoutParams = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
            layoutParams.setMargins(0, 0, 8.dpToPx(), 0)
            imageView.layoutParams = layoutParams

            imageView.setImageURI(Uri.parse(imageUriString)) // Set the image URI directly

            linearLayout.addView(imageView, layoutParams)


            val imageUri = Uri.parse(imageUriString)
            imageView.setImageURI(imageUri)

            llPhotos.addView(imageView)
        }
    }
    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }


    private fun setupActionBar(){
        val toolbarNewActivity : Toolbar = findViewById(R.id.toolbar_detail_activity)
        setSupportActionBar(toolbarNewActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            //This will make own action clickable and the "<-" at the left side
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back)
        }
        toolbarNewActivity.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun deleteEntrySuccess() {
        showCustomSnackbar("Entry deleted!",false)
        onBackPressedDispatcher.onBackPressed()
    }
}
