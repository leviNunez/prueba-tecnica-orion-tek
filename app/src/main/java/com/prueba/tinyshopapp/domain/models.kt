package com.prueba.tinyshopapp.domain

import com.prueba.tinyshopapp.data.database.AddressList
import com.prueba.tinyshopapp.data.database.CustomerEntity

data class Customer(
    val id: Long,
    val name: String,
    val addresses: List<Address>
)

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String
)
