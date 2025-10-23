package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object SuppliersData : Table("suppliers_data") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val supplierId = varchar("supplier_id", 36).default("")
    val productId = varchar("product_id", 36).default("")
    val title = varchar("title", 255)
    val quantity = integer("quantity").default(0)
    val image = varchar("image", 500)
    val purchasePrice = integer("purchase_price").default(0)
    val type = varchar("type", 20).default("INCOME")
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val raisedBy = varchar("raised_by", 255)
    val updatedBy = varchar("updated_by", 255)
    val updatedAt = long("updated_at").default(0L).index()
    val createdAt = long("created_at").default(0L)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}

