package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object Suppliers : Table("suppliers") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val name = varchar("name", 255)
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val raisedBy = varchar("raised_by", 255)
    val updatedBy = varchar("updated_by", 255)
    val updatedAt = long("updated_at").default(0L).index()
    val createdAt = long("created_at").default(0L)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}
