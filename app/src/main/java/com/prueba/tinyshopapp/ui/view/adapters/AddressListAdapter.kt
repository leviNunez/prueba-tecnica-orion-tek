package com.prueba.tinyshopapp.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.prueba.tinyshopapp.databinding.AddressListFooterItemBinding
import com.prueba.tinyshopapp.databinding.AddressListItemBinding
import com.prueba.tinyshopapp.domain.Address
import kotlinx.coroutines.*
import java.lang.ClassCastException

private const val ITEM_VIEW_TYPE_ADDRESS = 0
private const val ITEM_VIEW_TYPE_FOOTER = 1

class AddressListAdapter(private val clickListener: FooterItemListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DiffUtilCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_ADDRESS -> AddressViewHolder.from(parent)
            ITEM_VIEW_TYPE_FOOTER -> FooterViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.AddressItem -> ITEM_VIEW_TYPE_ADDRESS
            is DataItem.Footer -> ITEM_VIEW_TYPE_FOOTER
        }
    }

    fun addFooterAndSubmitList(list: List<Address>) {
        adapterScope.launch {
            val items = list.map { DataItem.AddressItem(it) } + listOf(DataItem.Footer)
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AddressViewHolder -> {
                val item = getItem(position) as DataItem.AddressItem
                holder.bind(item.address)
            }
            is FooterViewHolder -> {
                holder.bind(clickListener)
            }
        }

    }

    class AddressViewHolder private constructor(private val binding: AddressListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Address) {
            binding.address = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AddressViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AddressListItemBinding.inflate(layoutInflater, parent, false)
                return AddressViewHolder(binding)
            }
        }
    }

    class FooterViewHolder(private val binding: AddressListFooterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: FooterItemListener) {
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FooterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AddressListFooterItemBinding.inflate(layoutInflater, parent, false)

                return FooterViewHolder(binding)
            }
        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(
            oldItem: DataItem,
            newItem: DataItem
        ): Boolean {
            return oldItem.street == newItem.street
        }

        override fun areContentsTheSame(
            oldItem: DataItem,
            newItem: DataItem
        ): Boolean {
            return oldItem == newItem
        }

    }
}

sealed class DataItem {
    data class AddressItem(val address: Address) : DataItem() {
        override val street: String
            get() = address.street
    }

    object Footer : DataItem() {
        override val street: String
            get() = Long.MAX_VALUE.toString()
    }

    abstract val street: String
}

class FooterItemListener(val clickListener: () -> Unit) {
    fun onClick() = clickListener()
}
