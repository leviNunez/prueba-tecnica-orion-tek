package com.prueba.tinyshopapp.ui.viewmodel.newcustomer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prueba.tinyshopapp.domain.repository.CustomerRepository

class NewCustomerViewModelFactory(
    private val repository: CustomerRepository
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewCustomerViewModel::class.java)) {
            return NewCustomerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}