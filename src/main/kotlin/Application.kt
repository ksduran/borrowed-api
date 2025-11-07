package com.kevinduran

import com.kevinduran.config.database.DatabaseConfig
import com.kevinduran.infrastructure.plugins.configureValidation
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.repositories.EmployeesRepositoryImpl
import com.kevinduran.infrastructure.repositories.LicenseRepositoryImpl
import com.kevinduran.infrastructure.repositories.ProductEntriesRepositoryImpl
import com.kevinduran.infrastructure.repositories.ProductsRepositoryImpl
import com.kevinduran.infrastructure.repositories.SaleReturnsRepositoryImpl
import com.kevinduran.infrastructure.repositories.SalesRepositoryImpl
import com.kevinduran.infrastructure.repositories.SuppliersDataRepositoryImpl
import com.kevinduran.infrastructure.repositories.SuppliersRepositoryImpl
import com.kevinduran.infrastructure.repositories.TransferPaymentsRepositoryImpl
import com.kevinduran.infrastructure.services.EmployeesService
import com.kevinduran.infrastructure.services.LicenseService
import com.kevinduran.infrastructure.services.ProductEntriesService
import com.kevinduran.infrastructure.services.ProductsService
import com.kevinduran.infrastructure.services.SaleReturnsService
import com.kevinduran.infrastructure.services.SalesService
import com.kevinduran.infrastructure.services.SuppliersDataService
import com.kevinduran.infrastructure.services.SuppliersService
import com.kevinduran.infrastructure.services.TransferPaymentsService
import com.kevinduran.infrastructure.services.discord.DiscordMessages
import com.kevinduran.infrastructure.tasks.scheduleCleanup
import com.kevinduran.infrastructure.tasks.schedulePendingToToday
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {

    val config = environment.config

    //Repositories
    val licenseRepository = LicenseRepositoryImpl()
    val employeesRepository = EmployeesRepositoryImpl()
    val productsRepository = ProductsRepositoryImpl()
    val suppliersRepository = SuppliersRepositoryImpl()
    //services
    val licenseService = LicenseService(licenseRepository)
    val employeesService = EmployeesService(employeesRepository)
    val productsService = ProductsService(productsRepository)
    val productEntriesService = ProductEntriesService(ProductEntriesRepositoryImpl())
    val saleReturnsService = SaleReturnsService(SaleReturnsRepositoryImpl())
    val salesService = SalesService(SalesRepositoryImpl())
    val suppliersDataService = SuppliersDataService(SuppliersDataRepositoryImpl())
    val suppliersService = SuppliersService(suppliersRepository)
    val transferPaymentsService = TransferPaymentsService(TransferPaymentsRepositoryImpl())

    installPlugins(config)
    DatabaseConfig.init(config)
    configureValidation(licenseService)
    configureRouting(
        licenseService = licenseService,
        employeesService = employeesService,
        productsService = productsService,
        productEntriesService = productEntriesService,
        saleReturnsService = saleReturnsService,
        salesService = salesService,
        suppliersDataService = suppliersDataService,
        suppliersService = suppliersService,
        transferPaymentsService = transferPaymentsService
    )
    schedulePendingToToday(config)
    scheduleCleanup(config)
}

fun Application.installPlugins(config: ApplicationConfig) {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            this@installPlugins.log.error("Unexpected error", cause)

            this@installPlugins.launch {
                DiscordMessages.notifyFailure(
                    config = config,
                    error = cause
                )
            }
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ApiResponse(
                    success = false,
                    message = "${cause.message}"
                )
            )

        }
    }
}

