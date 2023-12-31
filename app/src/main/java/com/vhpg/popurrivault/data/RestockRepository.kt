package com.vhpg.popurrivault.data

import android.util.Log
import androidx.room.Transaction
import com.vhpg.popurrivault.data.db.dao.MovementDao
import com.vhpg.popurrivault.data.db.dao.OrderDao
import com.vhpg.popurrivault.data.db.dao.ProductDao
import com.vhpg.popurrivault.data.db.model.MovementEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity

class RestockRepository(
    private val orderDao: OrderDao,
    private val movementDao: MovementDao,
    private val productDao: ProductDao
    ){
    @Transaction
    suspend fun createRestock(order: OrderEntity,products: MutableList<ProductEntity>):Long{
        return try {
            products.forEach { product ->
                var mov = MovementEntity(
                    idProduct = product.id,
                    idOrder = order.id,
                    idSale = null,
                    stock = product.stock,
                    typeMov = "IN",
                    amount = product.cost,
                    profit = -(product.cost*product.stock)
                )
                Log.d("idProd_mov", "${product.id}")
                movementDao.insertMovement(mov)

                productDao.restockProduct(productId = product.id,product.stock,product.cost)

            }

            orderDao.insertOrder(order)
        }catch (e: Exception) {
            // Manejar cualquier excepción aquí
            Log.e("CreateRestock", "Error creating restock", e)
            -1 // Otra indicación de error
        }
    }


    suspend fun getAllOrders(): List<OrderEntity> = orderDao.getAllOrdersBySupplier()

    suspend fun updateOrder(order: OrderEntity){
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: OrderEntity){
        orderDao.deleteOrder(order)
    }
}