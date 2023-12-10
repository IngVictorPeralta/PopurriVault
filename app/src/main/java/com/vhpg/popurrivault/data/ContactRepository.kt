package com.vhpg.popurrivault.data

import com.vhpg.popurrivault.data.db.dao.ContactDao
import com.vhpg.popurrivault.data.db.model.ContactEntity

class ContactRepository(private val contactDao: ContactDao){

    suspend fun insertContact(contact: ContactEntity):Long{
       return contactDao.insertContact(contact)
    }

    /*suspend fun getAllSuppliers(): List<ContactEntity> = contactDao.getAllSuppliers()

    suspend fun getAllClients(): List<ContactEntity> = contactDao.getAllClients()

    suspend fun getAllContacts(): List<ContactEntity> = contactDao.getAllContacts()
    */

    suspend fun getAllContactsByType(typeContact: String): List<ContactEntity> = contactDao.getAllContactsByType(typeContact)

    suspend fun updateContact(contact: ContactEntity){
        contactDao.updateContact(contact)
    }

    suspend fun deleteContact(contact: ContactEntity){
        contactDao.deleteContact(contact)
    }
}