package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.SuppliersData
import com.kevinduran.domain.models.SupplierData
import com.kevinduran.domain.repositories.SuppliersDataRepository
import com.kevinduran.infrastructure.mappers.toSupplierData
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class SuppliersDataRepositoryImpl : SuppliersDataRepository {
    override fun getBatch(
        license: String,
        lastSync: Long
    ): List<SupplierData> {
        return transaction {
            SuppliersData.selectAll().where {
                (SuppliersData.license eq license) and (SuppliersData.updatedAt greaterEq lastSync)
            }.map { it.toSupplierData() }
        }
    }

    override fun putBatch(
        license: String,
        data: List<SupplierData>
    ) {
        transaction {
            SuppliersData.batchUpsert(
                data = data,
                onUpdateExclude = listOf(SuppliersData.uuid, SuppliersData.createdAt),
                body = { sData ->
                    this[SuppliersData.uuid] = sData.uuid
                    this[SuppliersData.license] = sData.license
                    this[SuppliersData.supplierId] = sData.supplierId
                    this[SuppliersData.productId] = sData.productId
                    this[SuppliersData.title] = sData.title
                    this[SuppliersData.quantity] = sData.quantity
                    this[SuppliersData.image] = sData.image
                    this[SuppliersData.purchasePrice] = sData.purchasePrice
                    this[SuppliersData.type] = sData.type
                    this[SuppliersData.syncStatus] = sData.syncStatus
                    this[SuppliersData.deleted] = sData.deleted
                    this[SuppliersData.raisedBy] = sData.raisedBy
                    this[SuppliersData.updatedBy] = sData.updatedBy
                    this[SuppliersData.updatedAt] = sData.updatedAt
                    this[SuppliersData.createdAt] = sData.createdAt
                }
            )
        }
    }
}