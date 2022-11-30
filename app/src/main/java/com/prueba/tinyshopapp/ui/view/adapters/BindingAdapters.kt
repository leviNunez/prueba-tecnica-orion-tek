package com.prueba.tinyshopapp.ui.view

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.prueba.tinyshopapp.R
import com.prueba.tinyshopapp.domain.Customer
import com.prueba.tinyshopapp.utils.RecyclerViewSpacer


@BindingAdapter("showOnlyWhenEmpty")
fun View.showOnlyWhenEmpty(data: List<Customer>?) {
    visibility = when {
        data == null || data.isEmpty() -> View.VISIBLE
        else -> View.GONE
    }
}

@BindingAdapter("customerAddress")
fun TextView.formatAddress(customer: Customer) {
    val address = customer.addresses[0]

    text = context.getString(
        R.string.formatted_address,
        address.street,
        address.city,
        address.state,
        address.zipCode,
        address.country
    )
}

@BindingAdapter("recyclerViewItemSpacer")
fun RecyclerView.bindItemDecoratorToRecyclerView(spaceBottom: Float) {
    addItemDecoration(RecyclerViewSpacer(spaceBottom.toInt()))
}


