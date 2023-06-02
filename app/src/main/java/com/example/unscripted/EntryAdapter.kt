package com.example.unscripted


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EntryAdapter(private val dataList: MutableList<Pair<Date, String>>) :
    RecyclerView.Adapter<EntryAdapter.EntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.entry_item, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val (date, string) = dataList[position]
        holder.bind(date, string)

        holder.itemView.setOnClickListener {
            val selectedItem = dataList[position]
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("selectedItem", selectedItem)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.text_entry_item_date)
        private val stringTextView: TextView = itemView.findViewById(R.id.text_entry_title)

        fun bind(date: Date, string: String) {
            dateTextView.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
            stringTextView.text = string
        }
    }

    fun addEntry(entry: Pair<Date, String>) {
        dataList.add(entry)
        notifyItemInserted(dataList.size - 1)
    }
}