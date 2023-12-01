package com.vhpg.popurrivault.data

import com.vhpg.popurrivault.data.db.dao.SupplierDao
import com.vhpg.popurrivault.data.db.model.SupplierEntity

class SupplierRepository(private val supplierDao: SupplierDao){

    suspend fun insertSupplier(supplier: SupplierEntity){
        supplierDao.insertSupplier(supplier)
    }

    suspend fun getAllSuppliers(): List<SupplierEntity> = supplierDao.getAllSuppliers()

    suspend fun updateSupplier(supplier: SupplierEntity){
        supplierDao.updateSupplier(supplier)
    }

    suspend fun deleteSupplier(supplier: SupplierEntity){
        supplierDao.deleteSupplier(supplier)
    }
}