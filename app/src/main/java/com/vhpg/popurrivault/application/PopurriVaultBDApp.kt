package com.vhpg.popurrivault.application

import android.app.Application
import com.vhpg.popurrivault.data.ContactRepository
import com.vhpg.popurrivault.data.RestockRepository
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.SaleRepository


import com.vhpg.popurrivault.data.db.PopurriVaultDatabase


class PopurriVaultBDApp(): Application() {
    private val database by lazy {
        PopurriVaultDatabase.getDatabase(this@PopurriVaultBDApp)
    }

    val productRepository by lazy{
        ProductRepository(database.productDao())
    }

    val contactRepository by lazy {
        ContactRepository(database.contactDao())
    }

    val restockRepository by lazy {
        RestockRepository(database.orderDao(),database.movementDao(),database.productDao())
    }

    val saleRepository by lazy {
        SaleRepository(database.saleDao(),database.movementDao(),database.productDao())
    }
}