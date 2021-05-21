package com.astrocode.budgetplanner.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable
import java.math.BigDecimal
import java.util.*

@TypeConverters(Converters::class)
@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),

    @ColumnInfo(name = "modified_at")
    val modifiedAt: Date = Date(),

    @ColumnInfo(name = "date")
    val date: Date,

    @ColumnInfo(name = "type")
    val type: EntryType,

    @ColumnInfo(name = "category")
    val category: EntryCategory,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "amount")
    val amount: BigDecimal
): Serializable
