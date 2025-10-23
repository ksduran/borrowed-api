package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.Product

interface ProductsRepository {
    fun getBatch(license: String, lastSync: Long): List<Product>
    fun putBatch(license: String, products: List<Product>)
}