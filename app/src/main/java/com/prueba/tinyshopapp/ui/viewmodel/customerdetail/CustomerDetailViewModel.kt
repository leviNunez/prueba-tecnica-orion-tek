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
    private val state: SavedStateHandle
) :
    ViewModel() {
    private val _navigateToNewAddress = MutableSharedFlow<Long>()
    private val _navigateToHome = MutableSharedFlow<Int>()

    private val customer = MediatorLiveData<Customer>()

    fun getCustomer() = customer

    val navigateToNewAddress = _navigateToNewAddress.asSharedFlow()
    val navigateToHome = _navigateToHome.asSharedFlow()

    init {
        viewModelScope.launch {
            state.get<Long>(CUSTOMER_ID)?.let { id ->
                customer.addSource(repository.getCustomerById(id), customer::setValue)
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
                val toDelete = CustomerEntity(
                    id = customer.id,
                    name = customer.name,
                    addressList = AddressList(addresses = customer.addresses)

                )
                val result = repository.deleteCustomer(toDelete)
                _navigateToHome.emit(result)
            }
        }
    }


}

private const val CUSTOMER_ID = "customerId"