package com.example.proba.ui   // если файл лежит в пакете ui, иначе замените на com.example.proba
import android.util.Log
import android.widget.Toast
import com.example.proba.ui.AddExpenseDialogFragment
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proba.R
import com.example.proba.databinding.ActivityMainBinding
import com.example.proba.viewmodel.ExpenseViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ExpenseViewModel by viewModels()
    private lateinit var adapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupBottomNavigation()
        setupFloatingButton()

        lifecycleScope.launch {
            viewModel.filteredExpenses.collect { expenses ->
                Log.d("FilterDebug", "Expenses: $expenses, size: ${expenses.size}")
                adapter.submitList(expenses)
                val total = viewModel.calculateTotal(expenses)
                binding.tvTotalAmount.text = "Общая сумма: $total ₽"
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter()
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_all -> {
                    Log.d("FilterDebug", "Selected All")
                    Toast.makeText(this, "Все", Toast.LENGTH_SHORT).show()
                    viewModel.setCategoryFilter(null)
                }
                R.id.menu_food -> {
                    Log.d("FilterDebug", "Selected Food")
                    Toast.makeText(this, "Еда", Toast.LENGTH_SHORT).show()
                    viewModel.setCategoryFilter("Еда")
                }
                R.id.menu_transport -> {
                    Log.d("FilterDebug", "Selected Transport")
                    Toast.makeText(this, "Транспорт", Toast.LENGTH_SHORT).show()
                    viewModel.setCategoryFilter("Транспорт")
                }
                R.id.menu_entertainment -> {
                    Log.d("FilterDebug", "Selected Entertainment")
                    Toast.makeText(this, "Развлечения", Toast.LENGTH_SHORT).show()
                    viewModel.setCategoryFilter("Развлечения")
                }
                R.id.menu_other -> {
                    Log.d("FilterDebug", "Selected Other")
                    Toast.makeText(this, "Другое", Toast.LENGTH_SHORT).show()
                    viewModel.setCategoryFilter("Другое")
                }
                else -> false
            }
            true
        }
    }

    private fun setupFloatingButton() {
        binding.fabAdd.setOnClickListener {
            AddExpenseDialogFragment().show(supportFragmentManager, "AddExpenseDialog")
        }
    }
}