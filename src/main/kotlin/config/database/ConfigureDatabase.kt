package com.kevinduran.config.database

import com.kevinduran.config.database.tables.Employees
import com.kevinduran.config.database.tables.Licenses
import com.kevinduran.config.database.tables.ProductEntries
import com.kevinduran.config.database.tables.Products
import com.kevinduran.config.database.tables.SaleReturns
import com.kevinduran.config.database.tables.Sales
import com.kevinduran.config.database.tables.Suppliers
import com.kevinduran.config.database.tables.SuppliersData
import com.kevinduran.config.database.tables.TransferPayments
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils.create
import org.jetbrains.exposed.v1.jdbc.transactions.transaction


object DatabaseConfig {

    fun init(config: ApplicationConfig): Database {
        val dbConfig = config.config("ktor.database")
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = dbConfig.property("url").getString()
            driverClassName = dbConfig.property("driver").getString()
            username = dbConfig.property("user").getString()
            password = dbConfig.property("password").getString()
            maximumPoolSize = 5
            isAutoCommit = false
            isReadOnly = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        val datasource = HikariDataSource(hikariConfig)
        val db = Database.connect(datasource)
        createTables(db)
        return db
    }

}

private fun createTables(db: Database) {
    transaction(db) {
        create(
            Employees, Licenses, ProductEntries, Products, SaleReturns, Sales,
            Suppliers, SuppliersData, TransferPayments
        )
    }
}