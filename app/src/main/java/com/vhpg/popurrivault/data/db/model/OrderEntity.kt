package com.vhpg.popurrivault.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants

@Entity(tableName =  Constants.DATABASE_ORDERS_TABLE)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val idProveedor: Long, // Foreign key de Proveedor
    val fecha: Long
)
