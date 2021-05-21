package com.astrocode.budgetplanner.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.astrocode.budgetplanner.R
import com.astrocode.budgetplanner.databinding.FragmentEntryBinding
import com.astrocode.budgetplanner.entities.Entry
import com.astrocode.budgetplanner.entities.EntryType
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

const val DATE_FORMAT = "dd-MM-yyyy HH:mm"

class EntryViewHolder (
    private val context: Context,
    private val binding: FragmentEntryBinding,
    private val adapter: EntryAdapter,
) : RecyclerView.ViewHolder(binding.root),
    View.OnClickListener,
    View.OnLongClickListener {
    private val logger by lazy { Logger.getLogger(this::class.java.simpleName) }

    fun bind(entry: Entry) {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
        simpleDateFormat.timeZone = TimeZone.getDefault()
        logger.info(TimeZone.getDefault().toString())

        with(binding) {
            entryTitle.text = entry.title
            entryDescription.text = entry.description
            entryCategory.text = entry.category.name
            entryAmount.text = entry.amount.toString()
            when (entry.type) {
                EntryType.INCOME -> {
                    typeIcon.setImageResource(R.drawable.income_icon)
                }
                EntryType.EXPANSE -> {
                    typeIcon.setImageResource(R.drawable.outcome_icone)
                }
            }
        }
        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        adapter.update(layoutPosition)
    }

    override fun onLongClick(v: View?): Boolean {
        return adapter.delete(layoutPosition)
    }
}