package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.SaleReturn
import com.kevinduran.domain.repositories.SaleReturnsRepository

class SaleReturnsService(private val repository: SaleReturnsRepository) {

    fun getBatch(license: String): List<SaleReturn> =
        repository.getBatch(license)

    fun putBatch(license: String, returns: List<SaleReturn>) = repository.putBatch(license, returns)

    fun deleteBatch(license: String, returns: List<SaleReturn>) =
        repository.deleteBatch(license, returns)

}