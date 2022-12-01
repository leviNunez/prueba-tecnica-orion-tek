package com.prueba.tinyshopapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.prueba.tinyshopapp.domain.Address
import com.prueba.tinyshopapp.domain.Customer

@Entity(tableName = "customers_table")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "customer_name")
    var name: String,

    @ColumnInfo(name = "customer_addresses")
    var addressList: AddressList

)

data class AddressList(
    val addresses: List<Address>
)


fun List<CustomerEntity>.asDomainModel(): List<Customer> {
    return map { customer ->
        Customer(
            id = customer.id,
            name = customer.name,
            addresses = customer.addressList.addresses
        )
    }
}

fun CustomerEntity.asDomainModel(): Customer =
    Customer(
        id = id,
        name = name,
        addresses = addressList.addresses
    )

class TypeConverters {
    @TypeConverter
    fun convertInvoiceListToJSONString(addressList: AddressList): String =
        Gson().toJson(addressList)

    @TypeConverter
    fun convertJSONStringToInvoiceList(jsonString: String): AddressList =
        Gson().fromJson(jsonString, AddressList::class.java)

}