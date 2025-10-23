package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table

object Sales : Table(name = "sales") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val productId = varchar("product_id", 36).default("")
    val supplierId = varchar("supplier_id", 36).default("")
    val changedProductId = varchar("changed_product_id", 36).default("")
    val companyName = varchar("company_name", 50).default("")
    val color = varchar("color", 50).default("")
    val size = varchar("size", 50).default("")
    val sizeR = varchar("size_r", 50).default("")
    val changedSize = varchar("changed_size", 50).default("")
    val changedProductColor = varchar("changed_product_color", 50).default("")
    val salePrice = integer("sale_price").default(0)
    val purchasePrice = integer("purchase_price").default(0)
    val raisedBy = varchar("raised_by", 255).default("")
    val updatedBy = varchar("updated_by", 255).default("")
    val paymentStatus = varchar("payment_status", 100).default("")
    val image = text("image")
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val updatedAt = long("updated_at").default(0).index()
    val createdAt = long("created_at").default(0)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}