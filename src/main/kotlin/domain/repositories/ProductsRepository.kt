package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.Product

interface ProductsRepository {
    fun getBatch(license: String): List<Product>
    fun putBatch(license: String, products: List<Product>)
    fun getById(license: String, id: String): Product?
    fun deleteBatch(license: String, products: List<Product>)
}