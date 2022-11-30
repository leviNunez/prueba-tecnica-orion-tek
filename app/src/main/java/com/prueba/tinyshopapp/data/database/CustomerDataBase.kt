package com.prueba.tinyshopapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(value = [com.prueba.tinyshopapp.data.database.TypeConverters::class])
@Database(entities = [CustomerEntity::class], version = 1, exportSchema = false)
abstract class CustomerDatabase() : RoomDatabase() {

    abstract val customerDatabaseDao: CustomerDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: CustomerDatabase? = null

        fun getInstance(context: Context): CustomerDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CustomerDatabase::class.java,
                        "sleep_history_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}