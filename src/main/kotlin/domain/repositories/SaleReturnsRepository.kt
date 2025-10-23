package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.SaleReturn

interface SaleReturnsRepository {
    fun getBatch(license: String, lastSync: Long): List<SaleReturn>
    fun putBatch(license: String, returns: List<SaleReturn>)
}