package com.example.proba.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba.data.AppDatabase
import com.example.proba.data.Expense
import com.example.proba.data.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.Date

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ExpenseRepository(
        AppDatabase.getInstance(application).expenseDao()
    )

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory

    private val allExpenses = repository.getAllExpenses()

    val filteredExpenses = combine(allExpenses, selectedCategory) { expenses, category ->
        if (category == null) expenses
        else expenses.filter { it.category == category }
    }

    fun setCategoryFilter(category: String?) {
        _selectedCategory.value = category
    }

    fun addExpense(amount: Double, category: String, date: Date, comment: String?) {
        viewModelScope.launch {
            repository.addExpense(
                Expense(
                    amount = amount,
                    category = category,
                    date = date,
                    comment = comment
                )
            )
        }
    }

    fun calculateTotal(expenses: List<Expense>): Double {
        return expenses.sumOf { it.amount }
    }
}