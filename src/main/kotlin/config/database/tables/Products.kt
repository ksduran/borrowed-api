package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object Products : Table(name = "products") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val name = varchar("name", 100)
    val ref = varchar("ref", 100)
    val salePrice = integer("sale_price").default(0)
    val purchasePrice = integer("purchase_price").default(0)
    val supplierName = varchar("supplier_name", 100).default("")
    val variants = text("variants")
    val imageSyncStatus = integer("image_sync_status").default(0)
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val raisedBy = varchar("raised_by", 255).default("")
    val updatedBy = varchar("updated_by", 255).default("")
    val updatedAt = long("updated_at").default(0).index()
    val createdAt = long("created_at").default(0)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}
