package com.example.unscripted

import Entry
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID


class NewEntryActivity : BasisActivity() {
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val GALLERY_PERMISSION_REQUEST_CODE = 101

    private var selectedImageView_smiley: ImageView? = null
    private var selectedImageView_weather: ImageView? = null

    private var myEntry: Entry? = null

    private var entryId: String? = null
    private var imageUris: MutableList<Uri> = mutableListOf()

    private lateinit var storageRef: StorageReference
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        db = FirebaseFirestore.getInstance()

        setupActionBar()

        this.entryId = UUID.randomUUID().toString()

        val pickImage: FloatingActionButton = findViewById(R.id.fabPhoto)

        pickImage.setOnClickListener {
            checkPermission()
        }

        val ok: FloatingActionButton = findViewById(R.id.fabOK)
        ok.setOnClickListener {
            if (createNewEntryCheck()) {
                createNewEntry()
            }
        }

        addIconOnClickListener()

        val textNewDate: TextView = findViewById(R.id.text_new_date)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)

        textNewDate.text = formattedDate
    }

    private fun createNewEntry() {
        val title: String  = findViewById<EditText?>(R.id.et_new_title).text.toString().trim { it <= ' ' }
        val text: String=  findViewById<EditText?>(R.id.et_new_text).text.toString().trim { it <= ' ' }

        myEntry = Entry()
        myEntry?.id =  entryId
        myEntry?.title = title
        myEntry?.date = Date()
        myEntry?.text = text
        myEntry?.mood = getSelectedMoodIconNumber()
        myEntry?.weather = getSelectedWeatherIconNumber()

        uploadImages()
        val cloudFirestore = CloudFirestore()
        cloudFirestore.saveEntryInfoOnCloudFirestore(this, myEntry!!)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, start image selection
            pickImageFromGallery()
        } else {
            // Request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start image selection
                pickImageFromGallery()
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable the functionality)
            }
        }
    }

    private fun addIconOnClickListener() {
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
    }

    private fun createNewEntryCheck(): Boolean {
        val title: String  = findViewById<EditText?>(R.id.et_new_title).text.toString().trim { it <= ' ' }
        val text: String=  findViewById<EditText?>(R.id.et_new_text).text.toString().trim { it <= ' ' }

        val moodIconNumber: Int = getSelectedMoodIconNumber()
        val weatherIconNumber: Int = getSelectedWeatherIconNumber()

        val returnValue = when {
            TextUtils.isEmpty(title) -> {
                showCustomSnackbar("Please enter a title.", true)
                false
            }
            text.isEmpty() -> {
                showCustomSnackbar("Please enter a text.", true)
                false
            }
            moodIconNumber == -1 -> {
                showCustomSnackbar("Please select an icon from the mood group.", true)
                false
            }
            weatherIconNumber == -1 -> {
                showCustomSnackbar("Please select an icon from the weather group.", true)
                false
            }
            else -> true
        }

        return returnValue
    }

    private fun getSelectedMoodIconNumber(): Int {
        val icon1ImageView: ImageView = findViewById(R.id.icon1ImageView)
        val icon2ImageView: ImageView = findViewById(R.id.icon2ImageView)
        val icon3ImageView: ImageView = findViewById(R.id.icon3ImageView)
        val icon4ImageView: ImageView = findViewById(R.id.icon4ImageView)
        val icon5ImageView: ImageView = findViewById(R.id.icon5ImageView)

        if (icon1ImageView.isSelected) {
            return 1
        } else if (icon2ImageView.isSelected) {
            return 2
        } else if (icon3ImageView.isSelected) {
            return 3
        } else if (icon4ImageView.isSelected) {
            return 4
        } else if (icon5ImageView.isSelected) {
            return 5
        }

        return -1
    }

    private fun getSelectedWeatherIconNumber(): Int {
        val iconWeather1ImageView: ImageView = findViewById(R.id.icon_weather_1_ImageView)
        val iconWeather2ImageView: ImageView = findViewById(R.id.icon_weather_2_ImageView)
        val iconWeather3ImageView: ImageView = findViewById(R.id.icon_weather_3_ImageView)
        val iconWeather4ImageView: ImageView = findViewById(R.id.icon_weather_4_ImageView)
        val iconWeather5ImageView: ImageView = findViewById(R.id.icon_weather_5_ImageView)

        if (iconWeather1ImageView.isSelected) {
            return 1
        } else if (iconWeather2ImageView.isSelected) {
            return 2
        } else if (iconWeather3ImageView.isSelected) {
            return 3
        } else if (iconWeather4ImageView.isSelected) {
            return 4
        } else if (iconWeather5ImageView.isSelected) {
            return 5
        }

        return -1
    }


    private fun handleIconClick(clickedImageView: ImageView) {
        selectedImageView_smiley?.isSelected = false

        clickedImageView.isSelected = true
        selectedImageView_smiley = clickedImageView
    }
    private fun handleIconClickWeather(clickedImageView: ImageView) {
        selectedImageView_weather?.isSelected = false

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
            val sizeInPx = 100.dpToPx()

            val layoutParams = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
            layoutParams.setMargins(0, 0, 8.dpToPx(), 0)
            imageView.layoutParams = layoutParams

            imageView.setImageURI(imgUri)

            linearLayout.addView(imageView, layoutParams)

            imageUris.add((imgUri!!))
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

  private fun uploadImages() {
      val entryId = this.entryId

      if (entryId == null) {
          // Entry ID not available, handle the error scenario
          return
      }

      for (imageUri in imageUris) {
          val imageId = UUID.randomUUID().toString()
          val fileName = "$entryId/$imageId.jpg"
          val uploadRef = storageRef.child("images/$fileName")

          // Upload each image to Firebase Storage
          val uploadTask = uploadRef.putFile(imageUri)

          uploadTask.addOnSuccessListener { taskSnapshot ->
              val imageUrl = taskSnapshot.metadata?.reference?.downloadUrl.toString()

              // Save the image URL to Firestore
              saveImageInfo(entryId, imageId, imageUrl)
          }.addOnFailureListener { exception ->
              // Handle the failure scenario
          }
      }
  }

    private fun saveImageInfo(entryId: String, imageId: String, imageUrl: String) {
        val imagesCollectionRef = db.collection("entries").document(entryId)
            .collection("images")

        val imageDocRef = imagesCollectionRef.document(imageId)
        val imageData = hashMapOf(
            "imageId" to imageId,
            "imageUrl" to imageUrl
        )
        imageDocRef.set(imageData)
            .addOnSuccessListener {
                // Image document created successfully with the download URL
            }
            .addOnFailureListener { exception ->
                // Handle unsuccessful document creation
            }
    }
    fun newEntrySuccess() {
        onBackPressedDispatcher.onBackPressed()
    }
}
