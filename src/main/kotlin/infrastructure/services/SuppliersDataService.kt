package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.SupplierData
import com.kevinduran.domain.repositories.SuppliersDataRepository

class SuppliersDataService(private val repository: SuppliersDataRepository) {

    fun getBatch(license: String): List<SupplierData> =
        repository.getBatch(license)

    fun putBatch(license: String, data: List<SupplierData>) = repository.putBatch(license, data)

    fun deleteBatch(license: String, data: List<SupplierData>) =
        repository.deleteBatch(license, data)

}