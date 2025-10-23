package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.Product
import com.kevinduran.domain.repositories.ProductsRepository

class ProductsService(private val repository: ProductsRepository) {

    fun getBatch(license: String, lastSync: Long): List<Product> =
        repository.getBatch(license, lastSync)

    fun putBatch(license: String, products: List<Product>) = repository.putBatch(license, products)

}