package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.Employees
import com.kevinduran.domain.models.Employee
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toEmployee() = Employee(
    uuid = this[Employees.uuid],
    license = this[Employees.license],
    name = this[Employees.name],
    user = this[Employees.user],
    chargeControl = this[Employees.chargeControl],
    createSales = this[Employees.createSales],
    createProducts = this[Employees.createProducts],
    createEmployees = this[Employees.createEmployees],
    createSuppliers = this[Employees.createSuppliers],
    updateSales = this[Employees.updateSales],
    updateProducts = this[Employees.updateProducts],
    updateEmployees = this[Employees.updateEmployees],
    updateSuppliers = this[Employees.updateSuppliers],
    deleteSales = this[Employees.deleteSales],
    deleteProducts = this[Employees.deleteProducts],
    deleteEmployees = this[Employees.deleteEmployees],
    deleteSuppliers = this[Employees.deleteSuppliers],
    fullSalesControl = this[Employees.fullSalesControl],
    fullProductsControl = this[Employees.fullProductsControl],
    fullEmployeesControl = this[Employees.fullEmployeesControl],
    fullSuppliersControl = this[Employees.fullSuppliersControl],
    seeSalesDetails = this[Employees.seeSalesDetails],
    seeProductsDetails = this[Employees.seeProductsDetails],
    seeSalesReports = this[Employees.seeSalesReports],
    seeFinanceReport = this[Employees.seeFinanceReport],
    status = this[Employees.status],
    syncStatus = this[Employees.syncStatus],
    deleted = this[Employees.deleted],
    raisedBy = this[Employees.raisedBy],
    updatedBy = this[Employees.updatedBy],
    updatedAt = this[Employees.updatedAt],
    createdAt = this[Employees.createdAt]
)
