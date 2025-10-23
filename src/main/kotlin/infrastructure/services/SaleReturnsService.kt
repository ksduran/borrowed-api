package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.SaleReturn
import com.kevinduran.domain.repositories.SaleReturnsRepository

class SaleReturnsService(private val repository: SaleReturnsRepository) {

    fun getBatch(license: String, lastSync: Long): List<SaleReturn> =
        repository.getBatch(license, lastSync)

    fun putBatch(license: String, returns: List<SaleReturn>) = repository.putBatch(license, returns)

}