package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.ProductEntries
import com.kevinduran.domain.models.ProductEntry
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toProductEntry() = ProductEntry(
    uuid = this[ProductEntries.uuid],
    license = this[ProductEntries.license],
    productId = this[ProductEntries.productId],
    image = this[ProductEntries.image],
    syncStatus = this[ProductEntries.syncStatus],
    deleted = this[ProductEntries.deleted],
    raisedBy = this[ProductEntries.raisedBy],
    updatedBy = this[ProductEntries.updatedBy],
    updatedAt = this[ProductEntries.updatedAt],
    createdAt = this[ProductEntries.createdAt]
)