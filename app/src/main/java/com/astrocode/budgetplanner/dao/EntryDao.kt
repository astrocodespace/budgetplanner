package com.astrocode.budgetplanner.dao

import androidx.room.*
import com.astrocode.budgetplanner.entities.Entry

@Dao
interface EntryDao {

    @get:Query("SELECT * FROM entries")
    val getAll: List<Entry>;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(entry: Entry)

    @Delete
    fun deleteEntry(entry: Entry)
}