package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.Sales
import com.kevinduran.domain.models.Sale
import com.kevinduran.domain.repositories.SalesRepository
import com.kevinduran.infrastructure.mappers.toSale
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class SalesRepositoryImpl : SalesRepository {
    override fun getBatch(
        license: String,
        lastSync: Long
    ): List<Sale> {
        return transaction {
            Sales.selectAll().where {
                (Sales.license eq license) and (Sales.updatedAt greaterEq lastSync)
            }.map { it.toSale() }
        }
    }

    override fun putBatch(
        license: String,
        sales: List<Sale>
    ) {
        transaction {
            Sales.batchUpsert(
                data = sales,
                onUpdateExclude = listOf(Sales.uuid, Sales.createdAt),
                body = { sale ->
                    this[Sales.uuid] = sale.uuid
                    this[Sales.license] = sale.license
                    this[Sales.productId] = sale.productId
                    this[Sales.supplierId] = sale.supplierId
                    this[Sales.changedProductId] = sale.changedProductId
                    this[Sales.companyName] = sale.companyName
                    this[Sales.color] = sale.color
                    this[Sales.size] = sale.size
                    this[Sales.sizeR] = sale.sizeR
                    this[Sales.changedSize] = sale.changedSize
                    this[Sales.changedProductColor] = sale.changedProductColor
                    this[Sales.salePrice] = sale.salePrice
                    this[Sales.purchasePrice] = sale.purchasePrice
                    this[Sales.raisedBy] = sale.raisedBy
                    this[Sales.updatedBy] = sale.updatedBy
                    this[Sales.paymentStatus] = sale.paymentStatus
                    this[Sales.image] = sale.image
                    this[Sales.syncStatus] = sale.syncStatus
                    this[Sales.deleted] = sale.deleted
                    this[Sales.updatedAt] = sale.updatedAt
                    this[Sales.createdAt] = sale.createdAt
                }
            )
        }
    }
}