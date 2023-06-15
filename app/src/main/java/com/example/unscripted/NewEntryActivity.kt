package com.example.unscripted

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NewEntryActivity : BasisActivity() {
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val GALLERY_PERMISSION_REQUEST_CODE = 101

    private var selectedImageView_smiley: ImageView? = null
    private var selectedImageView_weather: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

        setupActionBar()

        val pickImage: FloatingActionButton = findViewById(R.id.fabPhoto)

        pickImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, start image selection
                pickImageFromGallery()
            } else {
                // Request permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    GALLERY_PERMISSION_REQUEST_CODE
                )
            }
        }

        val ok: FloatingActionButton = findViewById(R.id.fabOK)

        ok.setOnClickListener {
          //  val ok: FloatingActionButton = findViewById(R.id.new)
        }

        val icon1ImageView: ImageView = findViewById(R.id.icon1ImageView)
        val icon2ImageView: ImageView = findViewById(R.id.icon2ImageView)
        val icon3ImageView: ImageView = findViewById(R.id.icon3ImageView)
        val icon4ImageView: ImageView = findViewById(R.id.icon4ImageView)
        val icon5ImageView: ImageView = findViewById(R.id.icon5ImageView)

        // Set click listeners for each ImageView item
        icon1ImageView.setOnClickListener {
            handleIconClick(icon1ImageView)
        }

        icon2ImageView.setOnClickListener {
            handleIconClick(icon2ImageView)
        }

        icon3ImageView.setOnClickListener {
            handleIconClick(icon3ImageView)
        }
        icon4ImageView.setOnClickListener {
            handleIconClick(icon4ImageView)
        }

        icon5ImageView.setOnClickListener {
            handleIconClick(icon5ImageView)
        }

        val icon_weather_1ImageView: ImageView = findViewById(R.id.icon_weather_1_ImageView)
        val icon_weather_2ImageView: ImageView = findViewById(R.id.icon_weather_2_ImageView)
        val icon_weather_3ImageView: ImageView = findViewById(R.id.icon_weather_3_ImageView)
        val icon_weather_4ImageView: ImageView = findViewById(R.id.icon_weather_4_ImageView)
        val icon_weather_5ImageView: ImageView = findViewById(R.id.icon_weather_5_ImageView)

        icon_weather_1ImageView.setOnClickListener {
            handleIconClickWeather(icon_weather_1ImageView)
        }

        icon_weather_2ImageView.setOnClickListener {
            handleIconClickWeather(icon_weather_2ImageView)
        }

        icon_weather_3ImageView.setOnClickListener {
            handleIconClickWeather(icon_weather_3ImageView)
        }
        icon_weather_4ImageView.setOnClickListener {
            handleIconClickWeather(icon_weather_4ImageView)
        }

        icon_weather_5ImageView.setOnClickListener {
            handleIconClickWeather(icon_weather_5ImageView)
        }

        val textNewDate: TextView = findViewById(R.id.text_new_date)

        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        textNewDate.text = formattedDate
    }

    private fun handleIconClick(clickedImageView: ImageView) {
        // Clear the selection if there is a previously selected ImageView
        selectedImageView_smiley?.isSelected = false

        // Set the clicked ImageView as the selected one
        clickedImageView.isSelected = true
        selectedImageView_smiley = clickedImageView
    }
    private fun handleIconClickWeather(clickedImageView: ImageView) {
        // Clear the selection if there is a previously selected ImageView
        selectedImageView_weather?.isSelected = false

        // Set the clicked ImageView as the selected one
        clickedImageView.isSelected = true
        selectedImageView_weather = clickedImageView
    }
    private fun pickImageFromGallery() {
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }

    private val changeImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imgUri = data?.data

            val linearLayout: LinearLayout = findViewById(R.id.ll_photos)

            val imageView = ImageView(this)
            //imageView.setImageURI(imgUri) // Set the desired image resource

            val sizeInPx = 100.dpToPx()

            val layoutParams = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
            layoutParams.setMargins(0, 0, 8.dpToPx(), 0)
            imageView.layoutParams = layoutParams

            val imagePath = imgUri // Replace with the actual path to your image

            Glide.with(this)
                .load(imagePath)
                .transform(CenterCrop()) // Crop to 1:1 and add rounded corners (optional)
                .into(imageView)

            linearLayout.addView(imageView, layoutParams)
        }
    }

    fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun setupActionBar(){
        val toolbarRegistrationActivity : Toolbar = findViewById(R.id.toolbar_new_activity)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
       /* super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start image selection*/
                pickImageFromGallery()
         /*   } else {
                // Permission denied
                showCustomSnackbar(getString(R.string.storage_permission_denied), true)
            }
        }*/
    }
}




