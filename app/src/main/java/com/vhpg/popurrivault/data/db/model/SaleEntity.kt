package com.vhpg.popurrivault.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants
import java.time.LocalDate

@Entity(tableName =  Constants.DATABASE_SALES_TABLE)
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "id_sale")
    val id: Long = 0,

    @ColumnInfo(name= "id_client")
    var idClient: Long?, // Foreign key de Proveedor

    @ColumnInfo(name= "sale_date_arrive")
    var dateArrive: Long,

    @ColumnInfo(name= "sale_arrived")
    var arrived: Boolean,

    @ColumnInfo(name= "sale_description")
    var description: String,

    @ColumnInfo(name= "sale_date_create")
    var dateOrder: Long,

    @ColumnInfo(name= "sale_price_total")
    var price: Double,
)
