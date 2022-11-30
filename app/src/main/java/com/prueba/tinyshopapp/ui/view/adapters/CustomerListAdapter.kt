package com.prueba.tinyshopapp.ui.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prueba.tinyshopapp.databinding.CustomerListItemBinding
import com.prueba.tinyshopapp.domain.Customer

class CustomerListAdapter(private val onClickListener: CustomerItemListener) :
    ListAdapter<Customer, CustomerListAdapter.ViewHolder>(DiffUtilCallback()) {

    class ViewHolder private constructor(private val binding: CustomerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Customer, clickListener: CustomerItemListener) {
            binding.customer = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomerListItemBinding.inflate(layoutInflater)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Customer>() {
        override fun areItemsTheSame(
            oldItem: Customer,
            newItem: Customer
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Customer,
            newItem: Customer
        ): Boolean {
            return oldItem == newItem
        }

    }
}

class CustomerItemListener(val clickListener: (customer: Customer) -> Unit) {
    fun onClick(customer: Customer) =
        clickListener(customer)
}