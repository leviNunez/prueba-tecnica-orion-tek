package com.prueba.tinyshopapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCustomer(customer: CustomerEntity): Long

    @Query("delete from customers_table where id = :key")
    suspend fun deleteCustomerById(key: Long): Int

    @Query("select * from customers_table order by id desc")
    fun getAllCustomers(): LiveData<List<CustomerEntity>>

    @Query("select * from customers_table where id = :key")
    suspend fun getCustomerById(key: Long): CustomerEntity
}