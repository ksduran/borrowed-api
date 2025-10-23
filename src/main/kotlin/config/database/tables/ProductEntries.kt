package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object ProductEntries : Table("products_entries") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val productId = varchar("product_id", 36)
    val image = text("image")
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val raisedBy = varchar("raised_by", 255).default("")
    val updatedBy = varchar("updated_by", 255).default("")
    val updatedAt = long("updated_at").default(0).index()
    val createdAt = long("created_at").default(0)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}
