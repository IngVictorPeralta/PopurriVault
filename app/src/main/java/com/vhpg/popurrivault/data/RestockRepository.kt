package com.vhpg.popurrivault.data

import androidx.room.Transaction
import com.vhpg.popurrivault.data.db.dao.MovementDao
import com.vhpg.popurrivault.data.db.dao.OrderDao
import com.vhpg.popurrivault.data.db.model.MovementEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity

class RestockRepository(
    private val orderDao: OrderDao,
    private val movementDao: MovementDao
    ){
    @Transaction
    suspend fun createRestock(order: OrderEntity,products: List<ProductEntity>):Long{

        products.forEach { product ->
            var mov = MovementEntity(
                idProduct = product.id,
                idOrder = order.id,
                idSale = null,
                stock = product.stock,
                typeMov = "IN",
                amount = product.price
            )

            movementDao.insertMovement(mov)
        }

       return orderDao.insertOrder(order)
    }


    suspend fun getAllOrders(): List<OrderEntity> = orderDao.getAllOrdersBySupplier()

    suspend fun updateOrder(order: OrderEntity){
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: OrderEntity){
        orderDao.deleteOrder(order)
    }
}