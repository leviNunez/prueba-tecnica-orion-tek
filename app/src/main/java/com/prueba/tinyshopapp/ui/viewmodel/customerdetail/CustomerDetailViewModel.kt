package com.prueba.tinyshopapp.ui.viewmodel.customerdetail

import androidx.lifecycle.*
import com.prueba.tinyshopapp.data.database.AddressList
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.domain.Customer
import com.prueba.tinyshopapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CustomerDetailViewModel(
    private val repository: CustomerRepository,
    state: SavedStateHandle
) :
    ViewModel() {
    private val _customer = MutableLiveData<Customer>()
    private val _navigateToNewAddress = MutableSharedFlow<Long>()
    private val _navigateToHome = MutableSharedFlow<Int>()

    val customer: LiveData<Customer> get() = _customer
    val navigateToNewAddress = _navigateToNewAddress.asSharedFlow()
    val navigateToHome = _navigateToHome.asSharedFlow()

    init {
        state.get<Long>(CUSTOMER_ID)?.let { id ->
            viewModelScope.launch {
                _customer.value = repository.getCustomerById(id)
            }
        }
    }

    fun onAddNewAddress() {
        viewModelScope.launch {
            customer.value?.let {
                _navigateToNewAddress.emit(it.id)
            }
        }
    }

    fun onDeleteCustomer() {
        viewModelScope.launch {
            customer.value?.let { customer ->
                val result = repository.deleteCustomer(customer.id)
                _navigateToHome.emit(result)
            }
        }
    }


}

private const val CUSTOMER_ID = "customerId"