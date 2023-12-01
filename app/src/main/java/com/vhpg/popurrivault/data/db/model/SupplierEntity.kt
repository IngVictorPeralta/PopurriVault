package com.vhpg.popurrivault.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants

@Entity(tableName =  Constants.DATABASE_SUPPLIERS_TABLE)
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "supplier_id")
    val id: Long = 0,

    @ColumnInfo(name= "supplier_name")
    var name: String,

    @ColumnInfo(name= "supplier_email")
    var email: String,

    @ColumnInfo(name= "supplier_phonenumber")
    var phoneNumber: Int,

    @ColumnInfo(name= "supplier_address")
    var address: Int,
)
