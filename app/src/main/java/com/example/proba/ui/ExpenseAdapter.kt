package com.example.proba.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proba.R
import com.example.proba.data.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    private var items: List<Any> = emptyList()

    fun submitList(expenses: List<Expense>) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val grouped = expenses.groupBy { dateFormat.format(it.date) }
        val newItems = mutableListOf<Any>()
        grouped.forEach { (date, list) ->
            newItems.add(date)
            newItems.addAll(list)
        }
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is String) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            val view = inflater.inflate(R.layout.item_date_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_expense, parent, false)
            ExpenseViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(items[position] as String)
            is ExpenseViewHolder -> holder.bind(items[position] as Expense)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDate: TextView = itemView.findViewById(R.id.tvDateHeader)
        fun bind(date: String) { tvDate.text = date }
    }

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvComment: TextView = itemView.findViewById(R.id.tvComment)

        fun bind(expense: Expense) {
            tvCategory.text = expense.category
            tvAmount.text = "${expense.amount} ₽"
            tvComment.text = expense.comment ?: ""
        }
    }
}