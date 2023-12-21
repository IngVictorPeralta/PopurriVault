package com.vhpg.popurrivault.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants

@Entity(tableName =  Constants.DATABASE_MOVEMENTS_TABLE)
data class MovementEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "movement_id")
    val id: Long = 0,
    @ColumnInfo(name= "product_id")
    val idProduct: Long,
    @ColumnInfo(name= "order_id")
    val idOrder: Long?,
    @ColumnInfo(name= "sale_id")
    val idSale: Long?,
    @ColumnInfo(name= "stock_product")
    val stock: Int,
    @ColumnInfo(name= "type_movement")
    val typeMov: String, // Puede ser "entrada" o "salida" u otros tipos según tus necesidades
    @ColumnInfo(name= "amount_product")
    val amount: Double,
    @ColumnInfo(name= "profit_movement")
    val profit: Double,


)
