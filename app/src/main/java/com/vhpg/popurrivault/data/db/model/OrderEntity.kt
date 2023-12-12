package com.vhpg.popurrivault.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants
import java.time.LocalDate

@Entity(tableName =  Constants.DATABASE_ORDERS_TABLE)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id_order")
    val id: Long = 0,

    @ColumnInfo(name= "id_supplier")
    var idSupplier: Long?, // Foreign key de Proveedor

    @ColumnInfo(name= "order_date_arrive")
    var dateArrive: Long,

    @ColumnInfo(name= "order_arrived")
    var arrived: Boolean,

    @ColumnInfo(name= "order_description")
    var description: String,

    @ColumnInfo(name= "order_date_create")
    var dateOrder: Long,

    @ColumnInfo(name= "order_cost_create")
    var cost: Double,
)
