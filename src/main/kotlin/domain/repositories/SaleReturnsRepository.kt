package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.SaleReturn

interface SaleReturnsRepository {
    fun getBatch(license: String): List<SaleReturn>
    fun putBatch(license: String, returns: List<SaleReturn>)
    fun deleteBatch(license: String, returns: List<SaleReturn>)
}