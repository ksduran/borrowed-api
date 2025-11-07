package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object Suppliers : Table("suppliers") {
    val uuid = varchar("uuid", 255)
    val license = varchar("license", 255)
    val name = varchar("name", 255)
    val debtControl = integer("debt_control").default(0)
    val syncStatus = integer("sync_status")
    val deleted = integer("deleted")
    val raisedBy = varchar("raised_by", 255)
    val updatedBy = varchar("updated_by", 255)
    val updatedAt = long("updated_at")
    val createdAt = long("created_at")

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}
