package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.SaleReturns
import com.kevinduran.domain.models.SaleReturn
import com.kevinduran.domain.repositories.SaleReturnsRepository
import com.kevinduran.infrastructure.mappers.toSaleReturn
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class SaleReturnsRepositoryImpl : SaleReturnsRepository {
    override fun getBatch(
        license: String
    ): List<SaleReturn> {
        return transaction {
            SaleReturns.selectAll().where {
                (SaleReturns.license eq license)
            }.map { it.toSaleReturn() }
        }
    }

    override fun putBatch(
        license: String,
        returns: List<SaleReturn>
    ) {
        transaction {
            SaleReturns.batchUpsert(
                data = returns,
                onUpdateExclude = listOf(SaleReturns.uuid, SaleReturns.createdAt),
                body = { data ->
                    this[SaleReturns.uuid] = data.uuid
                    this[SaleReturns.license] = data.license
                    this[SaleReturns.storeName] = data.storeName
                    this[SaleReturns.imagePath] = data.imagePath
                    this[SaleReturns.products] = data.products
                    this[SaleReturns.imageSyncStatus] = data.imageSyncStatus
                    this[SaleReturns.syncStatus] = data.syncStatus
                    this[SaleReturns.deleted] = data.deleted
                    this[SaleReturns.updatedAt] = data.updatedAt
                    this[SaleReturns.createdAt] = data.createdAt
                }
            )
        }
    }

    override fun deleteBatch(license: String, returns: List<SaleReturn>) {
        if (returns.isEmpty()) return
        transaction {
            val uuids = returns.map { it.uuid }
            SaleReturns.deleteWhere { SaleReturns.uuid inList uuids }
        }
    }
}