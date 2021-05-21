package com.astrocode.budgetplanner.adapter

import android.content.Intent
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.HandlerCompat
import com.astrocode.budgetplanner.MainActivity
import com.astrocode.budgetplanner.database.EntryDatabase
import androidx.recyclerview.widget.RecyclerView
import com.astrocode.budgetplanner.UpdateEntryActivity
import com.astrocode.budgetplanner.databinding.FragmentEntryBinding
import com.astrocode.budgetplanner.entities.Entry
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.concurrent.Executors

class EntryAdapter(
    private val activity: MainActivity,
    private val database: EntryDatabase,
) :
    RecyclerView.Adapter<EntryViewHolder>() {
    private val pool by lazy { Executors.newSingleThreadExecutor() }
    private val main = HandlerCompat.createAsync(Looper.getMainLooper())
    private var entries: List<Entry> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val viewBinding = FragmentEntryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EntryViewHolder(activity, viewBinding, this)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount(): Int = entries.size

    fun load() {
        pool.submit {
            entries = database.entries.getAll.sortedByDescending { it.date }
            val currentCalendar = Calendar.getInstance(TimeZone.getDefault())
            currentCalendar.time = Date()
            val balance = entries.stream()
                .filter {
                    val entryCalendar = Calendar.getInstance(TimeZone.getDefault())
                    entryCalendar.time = it.date
                    return@filter entryCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
                }
                .map { it.amount.setScale(2, RoundingMode.CEILING) }
                .reduce(BigDecimal.ZERO) { sum, element -> sum + element }
            activity.vm.balance.postValue("$balance")
            main.post { notifyDataSetChanged() }
        }
    }

    fun delete(position: Int): Boolean {
        val item = entries[position]

        AlertDialog.Builder(activity)
            .setTitle("Delete ${item.type}")
            .setMessage("Do you really want to remove this item?")
            .setPositiveButton(
                android.R.string.ok
            ) { _, _ ->
                pool.submit {
                    val result = database.entries.deleteEntry(item)
                    load()
                }
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()

        return true
    }

    fun update(position: Int) {
        val entry = entries[position]
        val intent = Intent(activity, UpdateEntryActivity::class.java)
        intent.putExtra("entry", entry)
        activity.startActivityForResult(intent, MainActivity.UPDATE_ENTRY)
    }
}