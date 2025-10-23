package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.Employees
import com.kevinduran.domain.models.Employee
import com.kevinduran.domain.repositories.EmployeesRepository
import com.kevinduran.infrastructure.mappers.toEmployee
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction


class EmployeesRepositoryImpl : EmployeesRepository {
    override fun getBatch(
        license: String,
        lastSync: Long
    ): List<Employee> {
        return transaction {
            Employees.selectAll().where {
                (Employees.license eq license)
            }.map { it.toEmployee() }
        }
    }

    override fun putBatch(
        license: String,
        employees: List<Employee>
    ) {
        if (employees.isEmpty()) return
        transaction {
            Employees.batchUpsert(
                data = employees,
                onUpdateExclude = listOf(Employees.createdAt, Employees.uuid),
                body = { employee ->
                    this[Employees.uuid] = employee.uuid
                    this[Employees.license] = employee.license
                    this[Employees.name] = employee.name
                    this[Employees.user] = employee.user
                    this[Employees.chargeControl] = employee.chargeControl
                    this[Employees.createSales] = employee.createSales
                    this[Employees.createProducts] = employee.createProducts
                    this[Employees.createEmployees] = employee.createEmployees
                    this[Employees.createSuppliers] = employee.createSuppliers
                    this[Employees.updateSales] = employee.updateSales
                    this[Employees.updateProducts] = employee.updateProducts
                    this[Employees.updateEmployees] = employee.updateEmployees
                    this[Employees.updateSuppliers] = employee.updateSuppliers
                    this[Employees.deleteSales] = employee.deleteSales
                    this[Employees.deleteProducts] = employee.deleteProducts
                    this[Employees.deleteEmployees] = employee.deleteEmployees
                    this[Employees.deleteSuppliers] = employee.deleteSuppliers
                    this[Employees.fullSalesControl] = employee.fullSalesControl
                    this[Employees.fullProductsControl] = employee.fullProductsControl
                    this[Employees.fullEmployeesControl] = employee.fullEmployeesControl
                    this[Employees.fullSuppliersControl] = employee.fullSuppliersControl
                    this[Employees.seeSalesDetails] = employee.seeSalesDetails
                    this[Employees.seeProductsDetails] = employee.seeProductsDetails
                    this[Employees.seeSalesReports] = employee.seeSalesReports
                    this[Employees.seeFinanceReport] = employee.seeFinanceReport
                    this[Employees.status] = employee.status
                    this[Employees.syncStatus] = employee.syncStatus
                    this[Employees.deleted] = employee.deleted
                    this[Employees.raisedBy] = employee.raisedBy
                    this[Employees.updatedBy] = employee.updatedBy
                    this[Employees.updatedAt] = employee.updatedAt
                    this[Employees.createdAt] = employee.createdAt
                }
            )
        }
    }

    override fun deleteBatch(
        license: String,
        employees: List<Employee>
    ) {
        if (employees.isEmpty()) return
        transaction {
            val uuids = employees.map { it.uuid }
            Employees.deleteWhere { Employees.uuid inList uuids }
        }
    }

}