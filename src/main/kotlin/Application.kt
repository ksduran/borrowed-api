package com.kevinduran

import com.kevinduran.config.database.DatabaseConfig
import com.kevinduran.config.firebase.configureMessaging
import com.kevinduran.infrastructure.plugins.configureValidation
import com.kevinduran.infrastructure.repositories.EmployeesRepositoryImpl
import com.kevinduran.infrastructure.repositories.LicenseRepositoryImpl
import com.kevinduran.infrastructure.repositories.ProductEntriesRepositoryImpl
import com.kevinduran.infrastructure.repositories.ProductsRepositoryImpl
import com.kevinduran.infrastructure.repositories.SaleReturnsRepositoryImpl
import com.kevinduran.infrastructure.repositories.SalesRepositoryImpl
import com.kevinduran.infrastructure.repositories.SuppliersDataRepositoryImpl
import com.kevinduran.infrastructure.repositories.TransferPaymentsRepositoryImpl
import com.kevinduran.infrastructure.services.EmployeesService
import com.kevinduran.infrastructure.services.LicenseService
import com.kevinduran.infrastructure.services.ProductEntriesService
import com.kevinduran.infrastructure.services.ProductsService
import com.kevinduran.infrastructure.services.SaleReturnsService
import com.kevinduran.infrastructure.services.SalesService
import com.kevinduran.infrastructure.services.SuppliersDataService
import com.kevinduran.infrastructure.services.TransferPaymentsService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    //Repositories
    val licenseRepository = LicenseRepositoryImpl()
    val employeesRepository = EmployeesRepositoryImpl()
    val productsRepository = ProductsRepositoryImpl()
    //services
    val licenseService = LicenseService(licenseRepository)
    val employeesService = EmployeesService(employeesRepository)
    val productsService = ProductsService(productsRepository)
    val productEntriesService = ProductEntriesService(ProductEntriesRepositoryImpl())
    val saleReturnsService = SaleReturnsService(SaleReturnsRepositoryImpl())
    val salesService = SalesService(SalesRepositoryImpl())
    val suppliersDataService = SuppliersDataService(SuppliersDataRepositoryImpl())
    val transferPaymentsService = TransferPaymentsService(TransferPaymentsRepositoryImpl())

    DatabaseConfig.init(environment.config)
    configureMessaging()
    installPlugins()
    configureValidation(licenseService)
    configureRouting(
        licenseService = licenseService,
        employeesService = employeesService,
        productsService = productsService,
        productEntriesService = productEntriesService,
        saleReturnsService = saleReturnsService,
        salesService = salesService,
        suppliersDataService = suppliersDataService,
        transferPaymentsService = transferPaymentsService
    )
}

fun Application.installPlugins() {
    install(ContentNegotiation) {
        json()
    }
}
