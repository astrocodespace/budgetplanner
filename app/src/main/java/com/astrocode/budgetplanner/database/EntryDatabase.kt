package com.astrocode.budgetplanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.astrocode.budgetplanner.dao.EntryDao
import com.astrocode.budgetplanner.entities.Entry

@Database(entities = [Entry::class], version = 1, exportSchema = true)
abstract class EntryDatabase: RoomDatabase() {
    abstract val entries: EntryDao

    companion object {
        var entryDatabase: EntryDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): EntryDatabase {
            if (entryDatabase != null) {
                entryDatabase = Room.databaseBuilder(
                    context,
                    EntryDatabase::class.java,
                    "entry.db"
                ).build()
            }

            return entryDatabase!!;
        }
    }
}