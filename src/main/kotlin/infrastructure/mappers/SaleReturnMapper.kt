package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.SaleReturns
import com.kevinduran.domain.models.SaleReturn
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toSaleReturn() = SaleReturn(
    uuid = this[SaleReturns.uuid],
    license = this[SaleReturns.license],
    storeName = this[SaleReturns.storeName],
    imagePath = this[SaleReturns.imagePath],
    products = this[SaleReturns.products],
    imageSyncStatus = this[SaleReturns.imageSyncStatus],
    syncStatus = this[SaleReturns.syncStatus],
    deleted = this[SaleReturns.deleted],
    updatedAt = this[SaleReturns.updatedAt],
    createdAt = this[SaleReturns.createdAt]
)