package com.prueba.tinyshopapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.prueba.tinyshopapp.data.database.CustomerDatabaseDao
import com.prueba.tinyshopapp.data.database.CustomerEntity
import com.prueba.tinyshopapp.data.database.asDomainModel
import com.prueba.tinyshopapp.domain.Customer
import com.prueba.tinyshopapp.domain.repository.CustomerRepository

class CustomerRepositoryImpl(private val customerDao: CustomerDatabaseDao) : CustomerRepository {
    override fun getAllCustomers(): LiveData<List<Customer>> =
        Transformations.map(customerDao.getAllCustomers()) { customers ->
            customers.asDomainModel()
        }

    override suspend fun addCustomer(customer: CustomerEntity): Long {
        return customerDao.insertCustomer(customer = customer)
    }

    override suspend fun updateCustomer(customer: CustomerEntity): Long {
        return customerDao.updateCustomer(customer = customer)
    }

    override suspend fun deleteCustomer(customerId: Long): Int {
        return customerDao.deleteCustomerById(key = customerId)
    }

    override suspend fun getCustomerById(customerId: Long): Customer =
        customerDao.getCustomerById(key = customerId).asDomainModel()

}