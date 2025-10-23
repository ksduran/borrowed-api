package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.Sale
import com.kevinduran.domain.repositories.SalesRepository

class SalesService(private val repository: SalesRepository) {

    fun getBatch(license: String, lastSync: Long): List<Sale> =
        repository.getBatch(license, lastSync)

    fun putBatch(license: String, sales: List<Sale>) = repository.putBatch(license, sales)

}