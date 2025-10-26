package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.Supplier

interface SuppliersRepository {
    fun getBatch(license: String): List<Supplier>
    fun putBatch(license: String, suppliers: List<Supplier>)
    fun deleteBatch(license: String, suppliers: List<Supplier>)
}
