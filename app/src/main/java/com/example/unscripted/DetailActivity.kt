// Load Detail of selected Entry
// last updated 22.06.2023
// Author Elisabeth Wadler
package com.example.unscripted

import Entry
import android.app.AlertDialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : BasisActivity() {
    private lateinit var storageRef: StorageReference
    private lateinit var db: FirebaseFirestore
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
        db = FirebaseFirestore.getInstance()
        progressBar = findViewById(R.id.progressBar)

        displayData()

        val btnDelete: FloatingActionButton = findViewById(R.id.fabDelete)
        btnDelete.setOnClickListener {
            deleteAction()
        }

        val btnEdit: FloatingActionButton = findViewById(R.id.fabEdit)
        btnEdit.setOnClickListener {
            showToastMessage(getString(R.string.implemented_soon))
        }
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun deleteAction() {
        val selectedItem = intent.getParcelableExtra<Entry>("selectedItem")

        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete_entry))
            .setMessage(getString(R.string.delete_question))
            .setPositiveButton("Delete") { _, _ ->
                val firestore = CloudFirestore()
                firestore.deleteEntryCloudFirestore(this, selectedItem!!)
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .create()

        dialog.show()
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
        textViewMood.text = textMood.toString()

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
        textViewWeather.text = textWeather.toString()

        val llPhotos: LinearLayout = findViewById(R.id.ll_photos)
        val imageUris: MutableList<String> = selectedItem?.imagePaths!!

        progressBar.visibility = View.VISIBLE
        retrieveImagesForEntry(selectedItem?.id!!)
    }

    private fun retrieveImagesForEntry(entryId: String) {
        val folderRef = storageRef.child("images/$entryId")

        folderRef.listAll()
            .addOnSuccessListener { listResult ->
                val linearLayout: LinearLayout = findViewById(R.id.ll_photos)

                for (imageRef in listResult.items) {
                    imageRef.downloadUrl
                        .addOnSuccessListener { downloadUrl ->
                            // Use the download URL to display or process the image
                            val imageUrl = downloadUrl.toString()

                            val imageView = ImageView(this)
                            val sizeInPx = 200.dpToPx()

                            val layoutParams = LinearLayout.LayoutParams(sizeInPx, sizeInPx)
                            layoutParams.setMargins(0, 0, 8.dpToPx(), 0)
                            imageView.layoutParams = layoutParams

                            Glide.with(this)
                                .load(imageUrl)
                                .transform(CenterCrop())
                                .into(imageView)


                            linearLayout.addView(imageView, layoutParams)
                        }
                        .addOnFailureListener {
                            showCustomSnackbar(getString(R.string.cant_load_img), true)
                        }
                }

                progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                showCustomSnackbar(getString(R.string.cant_load_img), true)
                progressBar.visibility = View.GONE
            }
    }


    private fun Int.dpToPx(): Int {
        return (this * Resources.getSystem().displayMetrics.density).toInt()
    }


    fun deleteEntrySuccess() {
        showCustomSnackbar(getString(R.string.deleted), false)
        onBackPressedDispatcher.onBackPressed()
    }
}
