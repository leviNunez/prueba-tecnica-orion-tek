package com.prueba.tinyshopapp.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.prueba.tinyshopapp.domain.Address
import com.prueba.tinyshopapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CustomerDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var customerDao: CustomerDatabaseDao
    private lateinit var db: CustomerDatabase
    private lateinit var fakeCustomer: CustomerEntity

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CustomerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        customerDao = db.customerDatabaseDao
        fakeCustomer = CustomerEntity(
            id = 1L,
            name = "John",
            addressList = AddressList(
                addresses = listOf(
                    Address(
                        street = "105 KROME AVE",
                        city = "MIAMI",
                        state = "FLORIDA",
                        zipCode = "33185-3700",
                        country = "United States"
                    )
                )
            )
        )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetAllCustomers() = runTest {
        customerDao.insertCustomer(fakeCustomer)
        val allCustomers = customerDao.getAllCustomers().getOrAwaitValue()

        assertThat(allCustomers).contains(fakeCustomer)
        assertThat(allCustomers[0].name).isEqualTo("John")
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCustomerById() = runTest {
        customerDao.insertCustomer(fakeCustomer)
        val customer = customerDao.getCustomerById(1L).getOrAwaitValue()

        assertThat(customer).isEqualTo(fakeCustomer)
        assertThat(customer.name).isEqualTo("John")
    }

    @Test
    @Throws(Exception::class)
    fun deleteCustomer() = runTest() {
        customerDao.insertCustomer(fakeCustomer)
        customerDao.deleteCustomerById(fakeCustomer)

        val allCustomers = customerDao.getAllCustomers().getOrAwaitValue()

        assertThat(allCustomers).doesNotContain(fakeCustomer)
    }
}