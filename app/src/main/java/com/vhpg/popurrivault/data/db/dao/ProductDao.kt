package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.util.Constants.DATABASE_PRODUCTS_TABLE
import java.text.DecimalFormat

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


    @Transaction
    suspend fun restockProduct(productId: Long, newStock: Int,itemsCost: Double) {
        // 1. Obtener el producto actual
        val product = getProductById(productId)

        // 2. Actualizar el stock del producto
        if (product != null) {
            val df = DecimalFormat("#.##")

            product.cost = df.format(  (((product.cost*product.stock) + (itemsCost*newStock))/(product.stock+newStock))  ).toDouble()
            product.stock += newStock
            updateProduct(product)
        }
    }

    @Transaction
    suspend fun saleProduct(productId: Long, itemsSaled: Int) {
        // 1. Obtener el producto actual
        val product = getProductById(productId)

        // 2. Actualizar el stock del producto
        if (product != null) {

            product.stock -= itemsSaled

            updateProduct(product)
        }
    }

    @Query("SELECT * FROM ${DATABASE_PRODUCTS_TABLE} WHERE product_id = :productId")
    suspend fun getProductById(productId: Long): ProductEntity?
}