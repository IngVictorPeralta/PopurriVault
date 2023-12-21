package com.vhpg.popurrivault.data

import android.util.Log
import androidx.room.Transaction
import com.vhpg.popurrivault.data.db.dao.MovementDao
import com.vhpg.popurrivault.data.db.dao.OrderDao
import com.vhpg.popurrivault.data.db.dao.ProductDao
import com.vhpg.popurrivault.data.db.dao.SaleDao
import com.vhpg.popurrivault.data.db.model.MovementEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.data.db.model.SaleEntity

class SaleRepository(
    private val saleDao: SaleDao,
    private val movementDao: MovementDao,
    private val productDao: ProductDao
    ){
    @Transaction
    suspend fun createSale(sale: SaleEntity,products: MutableList<ProductEntity>):Long{
        return try {
            products.forEach { product ->
                var mov = MovementEntity(
                    idProduct = product.id,
                    idOrder = null,
                    idSale = sale.id,
                    stock = product.stock,
                    typeMov = "OUT",
                    amount = product.cost,
                    profit = (product.price*product.stock)
                )
                Log.d("idProd_mov", "${product.id}")
                movementDao.insertMovement(mov)

                productDao.saleProduct(productId = product.id,product.stock)

            }

            saleDao.insertSale(sale)
        }catch (e: Exception) {
            // Manejar cualquier excepción aquí
            Log.e("CreateRestock", "Error creating restock", e)
            -1 // Otra indicación de error
        }
    }


    suspend fun getAllSales(): List<SaleEntity> = saleDao.getAllSalesByClient()

    suspend fun updateSale(sale: SaleEntity){
        saleDao.updateSale(sale)
    }

    suspend fun deleteSale(sale: SaleEntity){
        saleDao.deleteSale(sale)
    }
}