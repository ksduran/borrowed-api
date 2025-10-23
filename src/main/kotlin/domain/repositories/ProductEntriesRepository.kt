package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.ProductEntry

interface ProductEntriesRepository {
    fun getBatch(license: String, lastSync: Long): List<ProductEntry>
    fun putBatch(license: String, entries: List<ProductEntry>)
}