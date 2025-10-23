package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.SupplierData

interface SuppliersDataRepository {
    fun getBatch(license: String, lastSync: Long): List<SupplierData>
    fun putBatch(license: String, data: List<SupplierData>)
}