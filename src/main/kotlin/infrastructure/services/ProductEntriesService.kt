package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.ProductEntry
import com.kevinduran.domain.repositories.ProductEntriesRepository

class ProductEntriesService(private val repository: ProductEntriesRepository) {

    fun getBatch(license: String): List<ProductEntry> =
        repository.getBatch(license)

    fun putBatch(license: String, entries: List<ProductEntry>) =
        repository.putBatch(license, entries)

    fun deleteBatch(license: String, entries: List<ProductEntry>) =
        repository.deleteBatch(license, entries)

}