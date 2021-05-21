package com.astrocode.budgetplanner.entities

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

class Converters {
    @TypeConverter
    fun toCategory(value: String) = enumValueOf<EntryCategory>(value)

    @TypeConverter
    fun fromCategory(value: EntryCategory) = value.name

    @TypeConverter
    fun toType(value: String) = enumValueOf<EntryType>(value);

    @TypeConverter
    fun fromType(value: EntryType) = value.name;

    val utcTimeZone by lazy { TimeZone.getTimeZone("UTC") }

    @TypeConverter
    fun fromDate(value: String?): Date? {
        return value?.let {
            val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
            simpleDateFormat.timeZone = utcTimeZone
            simpleDateFormat.parse(it)
        }
    }

    @TypeConverter
    fun toDate(date: Date?): String? {
        return date?.let {
            val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
            simpleDateFormat.timeZone = utcTimeZone
            simpleDateFormat.format(it)
        }
    }

    @TypeConverter
    fun toBigDecimal(value: Double?): BigDecimal? {
        return value?.let {
            return it.toBigDecimal().setScale(2, RoundingMode.CEILING)
        }
    }

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): Double? {
        return value?.setScale(2, RoundingMode.CEILING)?.toDouble()
    }
}