package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.util.Constants.DATABASE_PRODUCTS_TABLE

@Dao
interface ProductDao {

    //Create
    @Insert
    suspend fun insertProduct(product: ProductEntity)

    //Read
    @Query("SELECT * FROM ${DATABASE_PRODUCTS_TABLE}")
    suspend fun getAllProducts(): List<ProductEntity>

    //Update
    @Update
    suspend fun updateProduct(product: ProductEntity)

    //Delete
    @Delete
    suspend fun deleteProduct(product: ProductEntity)
}