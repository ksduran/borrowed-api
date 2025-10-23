package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object Licenses : Table(name = "licenses") {
    val id = integer("id").autoIncrement()
    val code = varchar("code", 50).index()
    val active = integer("active").default(0)
    val packageName = varchar("package_name", 250).uniqueIndex()

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(id)
}