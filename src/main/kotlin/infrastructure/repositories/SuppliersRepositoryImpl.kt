package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.Suppliers
import com.kevinduran.domain.models.Supplier
import com.kevinduran.domain.repositories.SuppliersRepository
import com.kevinduran.infrastructure.mappers.toSupplier
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class SuppliersRepositoryImpl : SuppliersRepository {
    override fun getBatch(
        license: String
    ): List<Supplier> {
        return transaction {
            Suppliers.selectAll().where {
                (Suppliers.license eq license)
            }.map { it.toSupplier() }
        }
    }

    override fun putBatch(
        license: String,
        suppliers: List<Supplier>
    ) {
        transaction {
            Suppliers.batchUpsert(
                data = suppliers,
                onUpdateExclude = listOf(Suppliers.uuid, Suppliers.createdAt),
                body = { supplier ->
                    this[Suppliers.uuid] = supplier.uuid
                    this[Suppliers.license] = supplier.license
                    this[Suppliers.name] = supplier.name
                    this[Suppliers.debtControl] = supplier.debtControl
                    this[Suppliers.syncStatus] = supplier.syncStatus
                    this[Suppliers.deleted] = supplier.deleted
                    this[Suppliers.raisedBy] = supplier.raisedBy
                    this[Suppliers.updatedBy] = supplier.updatedBy
                    this[Suppliers.updatedAt] = supplier.updatedAt
                    this[Suppliers.createdAt] = supplier.createdAt
                }
            )
        }
    }

    override fun deleteBatch(license: String, suppliers: List<Supplier>) {
        if (suppliers.isEmpty()) return
        transaction {
            val uuids = suppliers.map { it.uuid }
            Suppliers.deleteWhere { Suppliers.uuid inList uuids }
        }
    }
}
