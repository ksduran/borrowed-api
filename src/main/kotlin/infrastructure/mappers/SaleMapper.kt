package com.kevinduran.infrastructure.mappers


import com.kevinduran.config.database.tables.Sales
import com.kevinduran.domain.models.Sale
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toSale() = Sale(
    uuid = this[Sales.uuid],
    license = this[Sales.license],
    productId = this[Sales.productId],
    supplierId = this[Sales.supplierId],
    changedProductId = this[Sales.changedProductId],
    companyName = this[Sales.companyName],
    color = this[Sales.color],
    size = this[Sales.size],
    sizeR = this[Sales.sizeR],
    changedSize = this[Sales.changedSize],
    changedProductColor = this[Sales.changedProductColor],
    salePrice = this[Sales.salePrice],
    purchasePrice = this[Sales.purchasePrice],
    raisedBy = this[Sales.raisedBy],
    updatedBy = this[Sales.updatedBy],
    paymentStatus = this[Sales.paymentStatus],
    image = this[Sales.image],
    syncStatus = this[Sales.syncStatus],
    deleted = this[Sales.deleted],
    updatedAt = this[Sales.updatedAt],
    createdAt = this[Sales.createdAt]
)
