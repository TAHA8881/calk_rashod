package com.example.proba.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.proba.R
import com.example.proba.viewmodel.ExpenseViewModel
import java.util.Date

class AddExpenseDialogFragment : DialogFragment() {

    private val viewModel: ExpenseViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_expense, null)

        val etAmount = view.findViewById<EditText>(R.id.etAmount)
        val etComment = view.findViewById<EditText>(R.id.etComment)
        val btnCategory = view.findViewById<TextView>(R.id.btnChooseCategory)

        var selectedCategory: String? = null

        btnCategory.setOnClickListener {
            val fragment = CategoryBottomSheetFragment { category ->
                selectedCategory = category
                btnCategory.text = category
            }
            fragment.show(parentFragmentManager, "CategoryBottomSheet")
        }

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавить трату")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
                val comment = etComment.text.toString()
                val category = selectedCategory ?: "Другое"
                if (amount > 0) {
                    viewModel.addExpense(amount, category, Date(), comment)
                }
            }
            .setNegativeButton("Отмена", null)
            .create()
    }
}