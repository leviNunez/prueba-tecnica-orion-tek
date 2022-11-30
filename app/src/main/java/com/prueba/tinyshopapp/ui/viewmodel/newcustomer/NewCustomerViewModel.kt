package com.prueba.tinyshopapp.ui.viewmodel.newcustomer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.domain.Customer
import com.prueba.tinyshopapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NewCustomerViewModel(private val repository: CustomerRepository) : ViewModel() {

    private val _navigateToHome = MutableSharedFlow<Long>()

    val navigateToHome = _navigateToHome.asSharedFlow()

    fun saveCustomer(customer: CustomerEntity) {
        viewModelScope.launch {
            val key = repository.addCustomer(customer)
            _navigateToHome.emit(key)
        }
    }
}