package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object SaleReturns : Table("sale_returns") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val storeName = varchar("store_name", 100)
    val imagePath = text("image_path")
    val products = text("products")
    val imageSyncStatus = integer("image_sync_status").default(0)
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val updatedAt = long("updated_at").default(0L).index()
    val createdAt = long("created_at").default(0L)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}
