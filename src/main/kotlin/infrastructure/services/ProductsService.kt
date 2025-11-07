package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.Product
import com.kevinduran.domain.repositories.ProductsRepository

class ProductsService(private val repository: ProductsRepository) {

    fun getBatch(license: String): List<Product> =
        repository.getBatch(license)

    fun getById(license: String, id: String): Product? =
        repository.getById(license, id)

    fun putBatch(license: String, products: List<Product>) = repository.putBatch(license, products)

    fun deleteBatch(license: String, products: List<Product>) =
        repository.deleteBatch(license, products)

}