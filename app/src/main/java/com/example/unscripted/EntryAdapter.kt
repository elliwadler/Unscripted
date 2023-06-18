package com.example.unscripted


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntryAdapter(private val context: Context, private val dataList: List<Entry>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.entry_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val title = dataList[position].title
        val date = dataList[position].date
        val text = dataList[position].text
        val imagePaths = dataList[position].imagePaths
        viewHolder.bind( date, title, text, imagePaths)

        return view!!
    }

    private class ViewHolder(view: View) {
        private val dateTextView: TextView = view.findViewById(R.id.text_entry_item_date)
        private val stringTextView: TextView = view.findViewById(R.id.text_entry_title)


        fun bind(date: Date?, title: String, text: String, image: MutableList<String>) {
            val formattedDate = date?.let {
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
            } ?: "N/A" // Provide a default value if date is null

            val formattedImage = image ?: "" // Provide a default value if image is null

            dateTextView.text = formattedDate
            stringTextView.text = title
            // Use the formattedImage variable where you need to set the image
            // For example, if you have an ImageView:
            // imageView.setImageURI(Uri.parse(formattedImage))
        }


    }
}