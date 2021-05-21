package com.astrocode.budgetplanner

import android.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.astrocode.budgetplanner.database.EntryDatabase
import com.astrocode.budgetplanner.databinding.ActivityAddEntryBinding
import com.astrocode.budgetplanner.fragment.DatePickerFragment
import com.astrocode.budgetplanner.view.EntryViewModel
import java.util.concurrent.Executors


class AddEntryActivity : AppCompatActivity() {

    private val pool by lazy { Executors.newSingleThreadExecutor() }
    private val binding by lazy { ActivityAddEntryBinding.inflate(layoutInflater) }
    private val viewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application)
            .create(EntryViewModel::class.java)
    }
    private val database by lazy { EntryDatabase.getDatabase(this) }

    companion object {
        const val BACK = 4
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, BACK)
        }

        setupSave()
    }

    private fun setupSave() = binding.create.setOnClickListener {
        if (viewModel.isFormValid()) {
            viewModel.toModel()?.let {
                pool.submit {
                    database.entries.insertEntry(it)
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    fun openDatePicker(view: View) {
        DatePickerFragment(binding.entryDate, viewModel.getDateTime()).show(
            supportFragmentManager,
            "datePicker"
        )
    }

}