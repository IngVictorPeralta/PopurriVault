package com.vhpg.popurrivault.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vhpg.popurrivault.data.db.dao.*
import com.vhpg.popurrivault.data.db.model.*
import com.vhpg.popurrivault.util.Constants

@Database(
    entities = [ OrderEntity::class,SaleEntity::class,ProductEntity::class,ContactEntity::class,MovementEntity::class],
    version = 2,
    exportSchema = true,
)
abstract class PopurriVaultDatabase: RoomDatabase(){

    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun saleDao(): SaleDao
    abstract fun contactDao(): ContactDao
    abstract fun movementDao(): MovementDao


    companion object{
        @Volatile
         private var INSTANCE: PopurriVaultDatabase? = null

        fun getDatabase(context: Context): PopurriVaultDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PopurriVaultDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}