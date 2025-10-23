package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.Products
import com.kevinduran.domain.models.Product
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toProduct() = Product(
    uuid = this[Products.uuid],
    license = this[Products.license],
    name = this[Products.name],
    ref = this[Products.ref],
    salePrice = this[Products.salePrice],
    purchasePrice = this[Products.purchasePrice],
    supplierName = this[Products.supplierName],
    variants = this[Products.variants],
    imageSyncStatus = this[Products.imageSyncStatus],
    syncStatus = this[Products.syncStatus],
    deleted = this[Products.deleted],
    raisedBy = this[Products.raisedBy],
    updatedBy = this[Products.updatedBy],
    updatedAt = this[Products.updatedAt],
    createdAt = this[Products.createdAt]
)