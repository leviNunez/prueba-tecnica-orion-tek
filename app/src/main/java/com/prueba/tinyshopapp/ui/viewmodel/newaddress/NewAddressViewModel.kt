package com.prueba.tinyshopapp.ui.viewmodel.newaddress

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prueba.tinyshopapp.data.database.AddressList
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.domain.Address
import com.prueba.tinyshopapp.domain.Customer
import com.prueba.tinyshopapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NewAddressViewModel(
    private val repository: CustomerRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _navigateToCustomerDetail = MutableSharedFlow<Long>()
    private val customer = MediatorLiveData<Customer>()

    val navigateToCustomerDetail = _navigateToCustomerDetail.asSharedFlow()

    fun getCustomer() = customer

    init {
        viewModelScope.launch {
            state.get<Long>(CUSTOMER_ID)
                ?.let { id ->
                    customer.addSource(repository.getCustomerById(id), customer::setValue)
                    println(customer.value)
                }
        }
    }

    fun updateCustomer(newAddress: Address) {
        viewModelScope.launch {
            customer.value?.let { customer ->
                println("Updating address...")
                val newCustomer = CustomerEntity(
                    id = customer.id,
                    name = customer.name,
                    addressList = AddressList(
                        addresses = customer.addresses + newAddress
                    )
                )
                println("New customer key ${newCustomer.id}")
                println("old customer key ${customer.id}")
                val key = repository.updateCustomer(newCustomer)
                _navigateToCustomerDetail.emit(key)
            }
        }
    }
}

private const val CUSTOMER_ID = "customerId"