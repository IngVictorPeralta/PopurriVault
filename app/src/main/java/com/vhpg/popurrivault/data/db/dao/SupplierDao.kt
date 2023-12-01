package com.vhpg.popurrivault.data.db.dao

import androidx.room.*
import com.vhpg.popurrivault.data.db.model.SupplierEntity
import com.vhpg.popurrivault.util.Constants
@Dao
interface SupplierDao {

    //Create
    @Insert
    suspend fun insertSupplier(supplier: SupplierEntity)

    //Read
    @Query("SELECT * FROM ${Constants.DATABASE_SUPPLIERS_TABLE}")
    suspend fun getAllSuppliers(): List<SupplierEntity>

    //Update
    @Update
    suspend fun updateSupplier(supplier: SupplierEntity)

    //Delete
    @Delete
    suspend fun deleteSupplier(supplier: SupplierEntity)

}