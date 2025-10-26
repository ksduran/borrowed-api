package com.kevinduran.config.routes

import com.kevinduran.domain.models.Sale
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.SalesService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.salesRoute(service: SalesService) {
    route("/sales") {

        get {
            val license = call.request.headers["X-License-Code"]!!
            val companyName = call.request.queryParameters["companyName"]
            val paymentStatus = call.request.queryParameters["paymentStatus"]
            val raisedBy = call.request.queryParameters["raisedBy"]
            val from = call.request.queryParameters["from"]
            val to = call.request.queryParameters["to"]

            if (companyName == null || companyName.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Company name parameter is required")
                )
                return@get
            }

            if (paymentStatus == null || paymentStatus.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Payment status parameter is required")
                )
                return@get
            }

            if (raisedBy == null || raisedBy.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Raised by parameter is required")
                )
                return@get
            }

            if (from == null || to == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "From and to parameters are required")
                )
                return@get
            }

            val allSales = service.getByRange(
                license = license,
                companyName = companyName,
                paymentStatus = paymentStatus,
                raisedBy = raisedBy,
                from = from.toLongOrNull() ?: 0L,
                to = to.toLongOrNull() ?: 0L
            )

            call.respond(allSales)
        }

        get("/today") {
            val license = call.request.headers["X-License-Code"]!!
            val companyName = call.request.queryParameters["companyName"]
            val paymentStatus = call.request.queryParameters["paymentStatus"]
            val raisedBy = call.request.queryParameters["raisedBy"]

            if (companyName == null || companyName.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Company name parameter is required")
                )
                return@get
            }

            if (paymentStatus == null || paymentStatus.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Payment status parameter is required")
                )
                return@get
            }

            if (raisedBy == null || raisedBy.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Raised by parameter is required")
                )
                return@get
            }

            val allSales = service.getToday(
                license = license,
                companyName = companyName,
                paymentStatus = paymentStatus,
                raisedBy = raisedBy,
            )
            call.respond(allSales)
        }

        get("/supplier") {
            val license = call.request.headers["X-License-Code"]!!
            val supplierId = call.request.queryParameters["supplierId"]
            val from = call.request.queryParameters["from"]
            val to = call.request.queryParameters["to"]

            if (supplierId == null || supplierId.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "supplier id parameter is required")
                )
                return@get
            }

            if (from == null || to == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "From and to parameters are required")
                )
                return@get
            }

            val allSales = service.getBySupplier(
                license = license,
                supplierId = supplierId,
                from = from.toLongOrNull() ?: 0L,
                to = to.toLongOrNull() ?: 0L
            )

            call.respond(allSales)
        }

        get("/statistics") {

            val license = call.request.headers["X-License-Code"]!!
            val productId = call.request.queryParameters["productId"]
            val from = call.request.queryParameters["from"]
            val to = call.request.queryParameters["to"]

            if (productId == null || productId.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Product id parameter is required")
                )
                return@get
            }

            if (from == null || to == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "From and to parameters are required")
                )
                return@get
            }

            val result = service.getStatistics(
                license = license,
                productId = productId,
                from = from.toLongOrNull() ?: 0,
                to = to.toLongOrNull() ?: 0
            )

            call.respond(result)
        }

        get("/earnings-summary") {
            val license = call.request.headers["X-License-Code"]!!
            val from = call.request.queryParameters["from"]
            val to = call.request.queryParameters["to"]

            if (from == null || to == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "From and to parameters are required")
                )
                return@get
            }

            val summary = service.getEarningsSummary(
                license = license,
                from = from.toLongOrNull() ?: 0L,
                to = to.toLongOrNull() ?: 0L
            )
            call.respond(summary)
        }

        get("/total-cash") {
            val license = call.request.headers["X-License-Code"]!!
            val raisedBy = call.request.queryParameters["raisedBy"]

            if (raisedBy == null || raisedBy.isBlank()) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Raised by parameter is required")
                )
                return@get
            }

            val total = service.getTotalCashByUser(
                license = license,
                raisedBy = raisedBy
            )
            call.respond(total)
        }

        post("/bulkDelete") {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val sales = call.receive<List<Sale>>()
                service.deleteBatch(license, sales)
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(true, "Deleted successfully")
                )
            } catch (e: Exception) {
                call.respond(ApiResponse(false, "Error: ${e.message}"))
            }
        }

        post {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val sales = call.receive<List<Sale>>()
                if (sales.isEmpty()) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, sales)
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(true, "Saved successfully")
                )
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Error: ${e.message}")
                )
            }
        }
    }
}