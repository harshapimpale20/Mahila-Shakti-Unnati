package com.example.mahilashaktiunnati

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// --- DATA MODELS (Defined here ONLY to avoid errors) ---
data class SavingEntry(
    val memberName: String = "",
    val amount: Double = 0.0,
    val paymentType: String = "",
    val status: String = "",
    val date: String = "",
    val week: String = "",
    val mobileNumber: String = "",
    val transactionId: String = ""
)

data class LoanEntry(
    val id: String = "",
    val memberName: String = "",
    val amount: Double = 0.0,
    val interestRate: Double = 0.0,
    val durationWeeks: Int = 0,
    val totalPayable: Double = 0.0,
    val remainingBalance: Double = 0.0,
    val status: String = "Pending",
    val date: String = ""
)

// --- STORAGE OBJECTS ---
object SavingStorage {
    private const val PREF_NAME = "saving_pref"
    private const val KEY_SAVING_LIST = "saving_list"

    fun load(context: Context): MutableList<SavingEntry> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(KEY_SAVING_LIST, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<SavingEntry>>() {}.type
        return Gson().fromJson(jsonString, type)
    }

    fun addEntry(context: Context, entry: SavingEntry) {
        val list = load(context)
        list.add(entry)
        val jsonString = Gson().toJson(list)
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_SAVING_LIST, jsonString).apply()
    }
}

object LoanStorage {
    private const val PREF_NAME = "loan_pref"
    private const val KEY_LOAN_LIST = "loan_list"

    fun loadLoans(context: Context): MutableList<LoanEntry> {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPreferences.getString(KEY_LOAN_LIST, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<LoanEntry>>() {}.type
        return Gson().fromJson(jsonString, type)
    }

    fun addLoan(context: Context, entry: LoanEntry) {
        val list = loadLoans(context)
        list.add(entry)
        val jsonString = Gson().toJson(list)
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
            .putString(KEY_LOAN_LIST, jsonString).apply()
    }
}