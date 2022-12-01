package com.prueba.tinyshopapp.ui.viewmodel.newaddress

import androidx.lifecycle.*
import com.prueba.tinyshopapp.data.database.AddressList
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.domain.Address
import com.prueba.tinyshopapp.domain.repository.CustomerRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NewAddressViewModel(
    private val repository: CustomerRepository,
    private val state: SavedStateHandle
) : ViewModel() {
    private val _navigateToCustomerDetail = MutableSharedFlow<Long>()

    val navigateToCustomerDetail = _navigateToCustomerDetail.asSharedFlow()

    fun updateCustomer(newAddress: Address) {
        viewModelScope.launch {
            state.get<Long>(CUSTOMER_ID)?.let { id ->
                val oldCustomer = repository.getCustomerById(id)
                val updatedCustomer = CustomerEntity(
                    id = oldCustomer.id,
                    name = oldCustomer.name,
                    addressList = AddressList(
                        addresses = oldCustomer.addresses + newAddress
                    )
                )
                val key = repository.updateCustomer(updatedCustomer)
                _navigateToCustomerDetail.emit(key)
            }

        }
    }
}

private const val CUSTOMER_ID = "customerId"