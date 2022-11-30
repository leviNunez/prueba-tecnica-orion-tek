package com.prueba.tinyshopapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCustomer(customer: CustomerEntity): Long

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity): Int

    @Query("select * from customer_table")
    fun getAllCustomers(): LiveData<List<CustomerEntity>>

    @Query("select * from customer_table where id = :key")
    fun getCustomerById(key: Long): LiveData<CustomerEntity>
}