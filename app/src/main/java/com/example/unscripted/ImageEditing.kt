package com.example.unscripted

import android.graphics.Bitmap

class ImageEditing {
    fun cropToSquare(bitmap:Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        val size = if (width > height) height else width

        val x = (width - size) / 2
        val y = (height - size) / 2

        return Bitmap.createBitmap(bitmap, x, y, size, size)
    }
}