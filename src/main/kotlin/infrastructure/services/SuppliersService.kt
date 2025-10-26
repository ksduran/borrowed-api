package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.Supplier
import com.kevinduran.domain.repositories.SuppliersRepository

class SuppliersService(private val repository: SuppliersRepository) {

    fun getBatch(license: String): List<Supplier> =
        repository.getBatch(license)

    fun putBatch(license: String, suppliers: List<Supplier>) =
        repository.putBatch(license, suppliers)

    fun deleteBatch(license: String, suppliers: List<Supplier>) =
        repository.deleteBatch(license, suppliers)

}
