package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.Suppliers
import com.kevinduran.domain.models.Supplier
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toSupplier() = Supplier(
    uuid = this[Suppliers.uuid],
    license = this[Suppliers.license],
    name = this[Suppliers.name],
    syncStatus = this[Suppliers.syncStatus],
    deleted = this[Suppliers.deleted],
    raisedBy = this[Suppliers.raisedBy],
    updatedBy = this[Suppliers.updatedBy],
    updatedAt = this[Suppliers.updatedAt],
    createdAt = this[Suppliers.createdAt]
)
