package com.astrocode.budgetplanner.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(private val input: EditText, private val dateTime: Date?) :
    DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance(TimeZone.getDefault())
        dateTime?.let {
            c.time = it
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(input.context, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = 0
        calendar.set(year, month, day)
        input.setText(
            "%1\$tY-%1\$tm-%1\$td".format(calendar),
            TextView.BufferType.EDITABLE
        )
    }
}