package com.example.unscripted

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class NewEntryActivity : BasisActivity() {
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val GALLERY_PERMISSION_REQUEST_CODE = 101

    private var selectedImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_entry)

        val pickImage: Button = findViewById(R.id.btn_new_addImage)
        selectedImage = findViewById(R.id.img_picked)

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
    }

    private fun pickImageFromGallery() {
        val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        changeImage.launch(pickImg)
    }

    private val changeImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imgUri = data?.data

            selectedImage?.setImageURI(imgUri)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GALLERY_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start image selection
                pickImageFromGallery()
            } else {
                // Permission denied
                showCustomSnackbar(getString(R.string.storage_permission_denied), true)
            }
        }
    }
}




