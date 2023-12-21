package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.SaleEntity
import com.vhpg.popurrivault.util.Constants

@Dao
interface SaleDao {
    //Create
    @Insert
    suspend fun insertSale(sale: SaleEntity): Long

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_SALES_TABLE}")
    suspend fun getAllSalesByClient(): List<SaleEntity>

    //Update
    @Update
    suspend fun updateSale(sale: SaleEntity)

    //Delete
    @Delete
    suspend fun deleteSale(sale: SaleEntity)

}