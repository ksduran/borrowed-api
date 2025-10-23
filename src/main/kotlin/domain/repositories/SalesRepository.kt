package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.Sale

interface SalesRepository {
    fun getBatch(license: String, lastSync: Long): List<Sale>
    fun putBatch(license: String, sales: List<Sale>)
}