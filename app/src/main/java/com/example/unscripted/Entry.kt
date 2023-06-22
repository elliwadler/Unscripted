// User class
// last updated 19.06.2023
// Author Elisabeth Wadler
import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Entry(
    var id: String? = "",
    var userId: String? = "",
    var title: String? = "",
    var date: Date? = Date(),
    var time: String? = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().time),
    var imagePaths: MutableList<String> = mutableListOf(),
    var text: String? = "",
    var mood: Int? = -1,
    var weather: Int? = -1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readSerializable() as? Date,
        parcel.readString(),
        mutableListOf<String>().apply {
            parcel.readList(this, String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeSerializable(date)
        parcel.writeString(time)
        parcel.writeList(imagePaths)
        parcel.writeString(text)
        parcel.writeValue(mood)
        parcel.writeValue(weather)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Entry> {
        override fun createFromParcel(parcel: Parcel): Entry {
            return Entry(parcel)
        }

        override fun newArray(size: Int): Array<Entry?> {
            return arrayOfNulls(size)
        }
    }
}
