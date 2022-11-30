package com.prueba.tinyshopapp.ui.viewmodel.home

import androidx.lifecycle.*
import com.prueba.tinyshopapp.domain.Customer
import com.prueba.tinyshopapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel(repository: CustomerRepository) : ViewModel() {

    private val _customers = repository.getAllCustomers()
    private val _navigateToNewCustomer = MutableSharedFlow<Boolean>()
    private val _navigateToCustomerDetail = MutableSharedFlow<Long>()

    val customers: LiveData<List<Customer>>
        get() = _customers

    val navigateToNewCustomer = _navigateToNewCustomer.asSharedFlow()

    val navigateToCustomerDetail = _navigateToCustomerDetail.asSharedFlow()

    fun onFabPressed() {
        viewModelScope.launch {
            _navigateToNewCustomer.emit(true)

        }
    }

    fun displayCustomerDetail(customerId: Long) {
        viewModelScope.launch {
            _navigateToCustomerDetail.emit(customerId)
        }
    }

}