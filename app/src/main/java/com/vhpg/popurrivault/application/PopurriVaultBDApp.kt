package com.vhpg.popurrivault.application

import android.app.Application
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.SupplierRepository

import com.vhpg.popurrivault.data.db.PopurriVaultDatabase


class PopurriVaultBDApp(): Application() {
    private val database by lazy {
        PopurriVaultDatabase.getDatabase(this@PopurriVaultBDApp)
    }

    val productRepository by lazy{
        ProductRepository(database.productDao())
    }

    val supplierRepository by lazy {
        SupplierRepository(database.supplierDao())
    }


}