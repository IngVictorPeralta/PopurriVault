package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.util.Constants
@Dao
interface ContactDao {

    //Create
    @Insert
    suspend fun insertContact(contact: ContactEntity): Long

    /*//Read
    @Query("SELECT * FROM ${Constants.DATABASE_CONTACTS_TABLE} WHERE contact_type='SUPPLIER'")
    suspend fun getAllSuppliers(): List<ContactEntity>

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_CONTACTS_TABLE} WHERE contact_type='CLIENT'")
    suspend fun getAllClients(): List<ContactEntity>

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_CONTACTS_TABLE}")
    suspend fun getAllContacts(): List<ContactEntity>
*/
    //READ
    @Query("SELECT * FROM ${Constants.DATABASE_CONTACTS_TABLE} WHERE contact_type=:type")
    suspend fun getAllContactsByType(type: String): List<ContactEntity>

    //Update
    @Update
    suspend fun updateContact(contact: ContactEntity)

    //Delete
    @Delete
    suspend fun deleteContact(contact: ContactEntity)

}