package com.prueba.tinyshopapp.domain.repository

import androidx.lifecycle.LiveData
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.domain.Customer

interface CustomerRepository {
    fun getAllCustomers(): LiveData<List<Customer>>

    suspend fun addCustomer(customer: CustomerEntity): Long

    suspend fun updateCustomer(customer: CustomerEntity): Long

    suspend fun deleteCustomer(customer: CustomerEntity): Int

    fun getCustomerById(customerId: Long): LiveData<Customer>
}