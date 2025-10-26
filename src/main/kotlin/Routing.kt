package com.kevinduran

import com.kevinduran.config.routes.employeeRoutes
import com.kevinduran.config.routes.healthRoutes
import com.kevinduran.config.routes.licensesRoutes
import com.kevinduran.config.routes.productEntriesRoute
import com.kevinduran.config.routes.productsRoutes
import com.kevinduran.config.routes.saleReturnsRoute
import com.kevinduran.config.routes.salesRoute
import com.kevinduran.config.routes.storageRoutes
import com.kevinduran.config.routes.suppliersDataRoute
import com.kevinduran.config.routes.suppliersRoutes
import com.kevinduran.config.routes.transferPaymentsRoute
import com.kevinduran.infrastructure.services.EmployeesService
import com.kevinduran.infrastructure.services.LicenseService
import com.kevinduran.infrastructure.services.ProductEntriesService
import com.kevinduran.infrastructure.services.ProductsService
import com.kevinduran.infrastructure.services.SaleReturnsService
import com.kevinduran.infrastructure.services.SalesService
import com.kevinduran.infrastructure.services.SuppliersDataService
import com.kevinduran.infrastructure.services.SuppliersService
import com.kevinduran.infrastructure.services.TransferPaymentsService
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.io.File

fun Application.configureRouting(
    licenseService: LicenseService,
    employeesService: EmployeesService,
    productsService: ProductsService,
    productEntriesService: ProductEntriesService,
    saleReturnsService: SaleReturnsService,
    salesService: SalesService,
    suppliersDataService: SuppliersDataService,
    suppliersService: SuppliersService,
    transferPaymentsService: TransferPaymentsService
) {
    routing {

        healthRoutes()
        employeeRoutes(employeesService)
        licensesRoutes(licenseService)
        productEntriesRoute(productEntriesService)
        productsRoutes(productsService)
        saleReturnsRoute(saleReturnsService)
        salesRoute(salesService)
        suppliersDataRoute(suppliersDataService)
        suppliersRoutes(suppliersService)
        transferPaymentsRoute(transferPaymentsService)
        storageRoutes()

        staticFiles(
            remotePath = "/files",
            dir = File("/var/duran-service/borrowed")
        )

        get("/docs") {
            call.respond(
                mapOf(
                    "GET /licenses?package=<package>&test=<bool>" to "Obtener licencia",
                    "POST /licenses" to "Crear licencia",
                    "POST /licenses/validate" to "Validar licencia",
                    "POST /licenses/activate?key=<licenseKey>" to "Activar licencia"
                )
            )
        }
    }
}
