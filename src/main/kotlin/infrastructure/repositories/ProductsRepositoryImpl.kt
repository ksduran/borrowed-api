package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.Products
import com.kevinduran.domain.models.Product
import com.kevinduran.domain.repositories.ProductsRepository
import com.kevinduran.infrastructure.mappers.toProduct
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class ProductsRepositoryImpl : ProductsRepository {
    override fun getBatch(
        license: String,
        lastSync: Long
    ): List<Product> {
        return transaction {
            Products.selectAll().where {
                (Products.license eq license) and (Products.updatedAt greaterEq lastSync)
            }.map { it.toProduct() }
        }
    }

    override fun putBatch(
        license: String,
        products: List<Product>
    ) {
        transaction {
            Products.batchUpsert(
                data = products,
                onUpdateExclude = listOf(Products.uuid, Products.createdAt),
                body = { product ->
                    this[Products.uuid] = product.uuid
                    this[Products.license] = product.license
                    this[Products.name] = product.name
                    this[Products.ref] = product.ref
                    this[Products.salePrice] = product.salePrice
                    this[Products.purchasePrice] = product.purchasePrice
                    this[Products.supplierName] = product.supplierName
                    this[Products.variants] = product.variants
                    this[Products.imageSyncStatus] = product.imageSyncStatus
                    this[Products.syncStatus] = product.syncStatus
                    this[Products.deleted] = product.deleted
                    this[Products.raisedBy] = product.raisedBy
                    this[Products.updatedBy] = product.updatedBy
                    this[Products.updatedAt] = product.updatedAt
                    this[Products.createdAt] = product.createdAt
                }
            )
        }
    }
}