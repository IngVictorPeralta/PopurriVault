package com.vhpg.popurrivault.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vhpg.popurrivault.util.Constants

@Entity(tableName =  Constants.DATABASE_CONTACTS_TABLE)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name= "contact_id")
    val id: Long = 0,

    @ColumnInfo(name= "contact_name")
    var name: String,

    @ColumnInfo(name= "contact_email")
    var email: String,

    @ColumnInfo(name= "contact_phoneNumber")
    var phoneNumber: String,

    @ColumnInfo(name= "contact_address")
    var address: String,

    @ColumnInfo(name= "contact_type")
    var type: String,

    @ColumnInfo(name = "supplier_credit_limit")
    var creditLimit: Double,


)
