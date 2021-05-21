package com.astrocode.budgetplanner.view

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.astrocode.budgetplanner.entities.Entry
import com.astrocode.budgetplanner.entities.EntryCategory
import com.astrocode.budgetplanner.entities.EntryType
import java.math.RoundingMode
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm"
const val DATE_FORMAT = "yyyy-MM-dd"
const val TIME_FORMAT = "HH:mm"

class EntryViewModel : ViewModel() {
    private val logger by lazy { Logger.getLogger(this::class.java.simpleName) }
    var entry: Entry? = null
    val amount = MutableLiveData<String>()
    val title = MutableLiveData<String>()
    val expanseType = MutableLiveData<Boolean>(false)
    val incomeType = MutableLiveData<Boolean>(false)
    val description = MutableLiveData<String>()
    val category = MutableLiveData<Int>()
    val date = MutableLiveData<String>()
    val balance = MutableLiveData<String>()

    private fun isAmountValid(): Boolean {
        amount.value?.let {
            return try {
                it.isNotBlank() && it.isNotEmpty() && !it.toDouble().isNaN() && it.toDouble() > 0
            } catch (e: NumberFormatException) {
                false
            }
        } ?: run { return false }
    }

    private fun isDescriptionValid(): Boolean {
        description.value?.let { return it.isNotBlank() && it.isNotEmpty() }
            ?: run { return false }
    }

    private fun isTitleValid(): Boolean {
        title.value?.let { return it.isNotBlank() && it.isNotEmpty() }
            ?: run { return false }
    }

    private fun isCategoryValid(): Boolean {
        category.value?.let { return it >= 1 } ?: run { return false }
    }

    private fun isDateValid(): Boolean {
        date.value?.let {
            val formatter = SimpleDateFormat(DATE_FORMAT)
            return try {
                formatter.parse(it)
                true
            } catch (e: ParseException) {
                false
            }
        } ?: run { return false }
    }

    fun isFormValid(): Boolean {
        logger.info(this.toString())
        return isAmountValid()
                && isDescriptionValid()
                && isCategoryValid()
                && isDateValid()
    }

    val valid = MediatorLiveData<Boolean>().apply {
        addSource(amount) {
            value = isFormValid()
        }
        addSource(description) {
            value = isFormValid()
        }
        addSource(category) {
            value = isFormValid()
        }
        addSource(date) {
            value = isFormValid()
        }
    }

    private fun getAmount(): Double? {
        if (isAmountValid()) {
            try {
                amount.value?.let {
                    return when (getType()) {
                        EntryType.INCOME -> it.toDouble()
                        EntryType.EXPANSE -> it.toDouble().unaryMinus()
                        else -> null
                    }
                }
            } catch (e: NumberFormatException) {
            }
        }
        return null
    }

    private fun getDescription(): String? {
        if (isDescriptionValid()) {
            description.value?.let { return it }
        }
        return null
    }

    private fun getCategory(): EntryCategory? {
        if (isCategoryValid()) {
            return category.value?.let { EntryCategory.values()[it] }
        }
        return null
    }

    private fun getTitle(): String? {
        if (isTitleValid()) {
            return title.value?.let { return it }
        }
        return null
    }

    fun getDateTime(): Date? {
        if (isDateValid()) {
            val formatter = SimpleDateFormat(DATETIME_FORMAT)
            formatter.timeZone = TimeZone.getDefault()
            try {
                return formatter.parse("%s".format(date.value))
            } catch (e: ParseException) {
            }
        }
        return null
    }

    fun toModel(): Entry? {
        val amount = getAmount()
        val description = getDescription()
        val category = getCategory()
        val dateTime = getDateTime()
        val type = getType();
        val title = title.value
        if (amount != null
            && type != null
            && description != null
            && category != null
            && dateTime != null
            && title != null
        ) {
            val entry = Entry(
                id = entry?.id ?: run { 0 },
                createdAt = entry?.createdAt ?: run { Date() },
                amount = amount.toBigDecimal().setScale(2, RoundingMode.CEILING),
                type = type,
                description = description,
                category = category,
                date = dateTime,
                title = title
            )
            return entry
        }
        return null
    }

    private fun getType(): EntryType? {
        if (incomeType.value == true) {
            return EntryType.INCOME
        }

        if (expanseType.value == true) {
            return EntryType.EXPANSE
        }

        return null;
    }

    fun basedOn(entry: Entry) {
        this.entry = entry
        val dateFormatter = SimpleDateFormat(DATE_FORMAT)
        dateFormatter.timeZone = TimeZone.getDefault()
        val timeFormatter = SimpleDateFormat(TIME_FORMAT)
        timeFormatter.timeZone = TimeZone.getDefault()

        date.value = dateFormatter.format(entry.date)
        amount.value = when (entry.type) {
            EntryType.INCOME -> entry.amount.toString()
            EntryType.EXPANSE -> entry.amount.unaryMinus().toString()
        }
        category.value = entry.category.ordinal
        description.value = entry.description
    }
}