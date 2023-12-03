package com.vhpg.popurrivault.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vhpg.popurrivault.data.db.dao.ContactDao
import com.vhpg.popurrivault.data.db.dao.OrderDao
import com.vhpg.popurrivault.data.db.dao.ProductDao
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.util.Constants

@Database(
    entities = [ OrderEntity::class,ProductEntity::class,ContactEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class PopurriVaultDatabase: RoomDatabase(){

    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun supplierDao(): ContactDao

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