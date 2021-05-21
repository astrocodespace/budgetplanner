package com.astrocode.budgetplanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.astrocode.budgetplanner.databinding.ActivityMainBinding
import com.astrocode.budgetplanner.view.EntryViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val vm by lazy {
        ViewModelProvider
            .AndroidViewModelFactory(application)
            .create(EntryViewModel::class.java)
    }

    companion object {
        const val ADD_ENTRY = 2
        const val UPDATE_ENTRY = 3
        const val REMOVE_ENTRY = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root);

        binding.fabBtnCreateEntry.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivityForResult(intent, ADD_ENTRY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (true) {
            (requestCode == ADD_ENTRY || requestCode == UPDATE_ENTRY)
                    && resultCode == Activity.RESULT_OK -> print("test")
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}