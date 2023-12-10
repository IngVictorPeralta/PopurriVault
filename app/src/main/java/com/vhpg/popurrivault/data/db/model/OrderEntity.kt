package com.vhpg.popurrivault.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants

@Entity(tableName =  Constants.DATABASE_ORDERS_TABLE)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id_order")
    val id: Long = 0,

    @ColumnInfo(name= "id_supplier")
    val idSupplier: Long, // Foreign key de Proveedor

    @ColumnInfo(name= "order_date_arrive")
    val dateArrive: Long,

    @ColumnInfo(name= "order_description")
    val description: String,

    @ColumnInfo(name= "order_date_create")
    val dateMov: Long,
)
