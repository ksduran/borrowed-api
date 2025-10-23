package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.SuppliersData
import com.kevinduran.domain.models.SupplierData
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toSupplierData() = SupplierData(
    uuid = this[SuppliersData.uuid],
    license = this[SuppliersData.license],
    supplierId = this[SuppliersData.supplierId],
    productId = this[SuppliersData.productId],
    title = this[SuppliersData.title],
    quantity = this[SuppliersData.quantity],
    image = this[SuppliersData.image],
    purchasePrice = this[SuppliersData.purchasePrice],
    type = this[SuppliersData.type],
    syncStatus = this[SuppliersData.syncStatus],
    deleted = this[SuppliersData.deleted],
    raisedBy = this[SuppliersData.raisedBy],
    updatedBy = this[SuppliersData.updatedBy],
    updatedAt = this[SuppliersData.updatedAt],
    createdAt = this[SuppliersData.createdAt]
)