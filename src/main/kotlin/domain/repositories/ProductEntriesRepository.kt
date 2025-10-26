package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.ProductEntry

interface ProductEntriesRepository {
    fun getBatch(license: String): List<ProductEntry>
    fun putBatch(license: String, entries: List<ProductEntry>)
    fun deleteBatch(license: String, entries: List<ProductEntry>)
}