package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.ProductEntries
import com.kevinduran.domain.models.ProductEntry
import com.kevinduran.domain.repositories.ProductEntriesRepository
import com.kevinduran.infrastructure.mappers.toProductEntry
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ProductEntriesRepositoryImpl : ProductEntriesRepository {
    override fun getBatch(
        license: String,
        lastSync: Long
    ): List<ProductEntry> {
        return transaction {
            ProductEntries.selectAll().where {
                (ProductEntries.license eq license) and (ProductEntries.updatedAt greaterEq lastSync)
            }.map { it.toProductEntry() }
        }
    }

    override fun putBatch(
        license: String,
        entries: List<ProductEntry>
    ) {
        transaction {
            if (entries.isEmpty()) return@transaction
            ProductEntries.batchUpsert(
                data = entries,
                onUpdateExclude = listOf(ProductEntries.uuid, ProductEntries.createdAt),
                body = { entry ->
                    this[ProductEntries.uuid] = entry.uuid
                    this[ProductEntries.license] = entry.license
                    this[ProductEntries.productId] = entry.productId
                    this[ProductEntries.image] = entry.image
                    this[ProductEntries.syncStatus] = entry.syncStatus
                    this[ProductEntries.deleted] = entry.deleted
                    this[ProductEntries.raisedBy] = entry.raisedBy
                    this[ProductEntries.updatedBy] = entry.updatedBy
                    this[ProductEntries.updatedAt] = entry.updatedAt
                    this[ProductEntries.createdAt] = entry.createdAt
                }
            )
        }
    }
}