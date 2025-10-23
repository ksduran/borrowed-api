package com.kevinduran.config.database.tables

import org.jetbrains.exposed.v1.core.Table


object Employees : Table(name = "employees") {
    val uuid = varchar("uuid", 36)
    val license = varchar("license", 50).index()
    val name = varchar("name", 100)
    val user = varchar("user", 100)
    val chargeControl = integer("charge_control").default(1)
    val createSales = integer("create_sales").default(0)
    val createProducts = integer("create_products").default(0)
    val createEmployees = integer("create_employees").default(0)
    val createSuppliers = integer("create_suppliers").default(0)
    val updateSales = integer("update_sales").default(0)
    val updateProducts = integer("update_products").default(0)
    val updateEmployees = integer("update_employees").default(0)
    val updateSuppliers = integer("update_suppliers").default(0)
    val deleteSales = integer("delete_sales").default(0)
    val deleteProducts = integer("delete_products").default(0)
    val deleteEmployees = integer("delete_employees").default(0)
    val deleteSuppliers = integer("delete_suppliers").default(0)
    val fullSalesControl = integer("full_sales_control").default(0)
    val fullProductsControl = integer("full_products_control").default(0)
    val fullEmployeesControl = integer("full_employees_control").default(0)
    val fullSuppliersControl = integer("full_suppliers_control").default(0)
    val seeSalesDetails = integer("see_sales_details").default(0)
    val seeProductsDetails = integer("see_products_details").default(0)
    val seeSalesReports = integer("see_sales_reports").default(0)
    val seeFinanceReport = integer("see_finance_report").default(0)
    val status = integer("status").default(0)
    val syncStatus = integer("sync_status").default(0)
    val deleted = integer("deleted").default(0).index()
    val raisedBy = varchar("raised_by", 255).default("")
    val updatedBy = varchar("updated_by", 255).default("@Administrador")
    val updatedAt = long("updated_at").default(0).index()
    val createdAt = long("created_at").default(0)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(uuid)
}

