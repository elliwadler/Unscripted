package com.example.unscripted

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Entry (
    val id:String? ="",
    var userId:String? ="",
    var title:String= "",
    var date:Date? = Date(),
    var time: String = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time),
    var imagePaths: MutableList<String> = mutableListOf(),
    var text:String = "",
    var mood:Int? =-1,
    var weather:Int?=-1,
)