package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.util.Constants

@Dao
interface OrderDao {
    //Create
    @Insert
    suspend fun insertOrder(order: OrderEntity): Long

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_ORDERS_TABLE}")
    suspend fun getAllOrdersBySupplier(): List<OrderEntity>
@Query("SELECT * FROM ${Constants.DATABASE_ORDERS_TABLE}")
    suspend fun getAllOrders(): List<OrderEntity>

    //Update
    @Update
    suspend fun updateOrder(order: OrderEntity)

    //Delete
    @Delete
    suspend fun deleteOrder(order: OrderEntity)

}