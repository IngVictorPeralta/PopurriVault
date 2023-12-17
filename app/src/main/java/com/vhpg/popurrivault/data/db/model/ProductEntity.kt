package com.vhpg.popurrivault.data.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName =  Constants.DATABASE_PRODUCTS_TABLE)
@Parcelize
data class ProductEntity(
    /*"name": "guantes",
    *"price": 199.99,
    *"cost": 119.99,
    *"description": "Guantes de cuero genuino ideales para el invierno.",
    *"category": "Ropa",
    *"stock": 87,
    *"lastRestockDate": "12/08/2023",
    *"url": "https://m.media-amazon.com/images/I/71Ac4COuieL._AC_SL1500_.jpg"
    */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "product_id")
    val id: Long = 0,

    @ColumnInfo(name= "product_name")
    var name: String,

    @ColumnInfo(name= "product_description")
    var description: String,

    @ColumnInfo(name= "product_url_image")
    var image: String,

    @ColumnInfo(name= "product_cost")
    var cost: Double,

    @ColumnInfo(name= "product_price")
    var price: Double,

    //@ColumnInfo(name= "product_category")
    //var category: Int,

    @ColumnInfo(name= "supplier_key")
    var supplier: Long?,

    @ColumnInfo(name= "product_stock")
    var stock: Int

    //@ColumnInfo(name= "product_last_restock_date")
    //val stock: Date,

    //@ColumnInfo(name= "product_url")
    //val url: String

) : Parcelable
