package com.kevinduran.config.routes

import com.kevinduran.domain.models.SaleReturn
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.SaleReturnsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json

fun Route.saleReturnsRoute(service: SaleReturnsService) {
    route("/sale-returns") {
        get {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val result = service.getBatch(license)
                call.respond(result)
            } catch (e: Exception) {
                call.respond(ApiResponse(false, "Error: ${e.message}"))
            }
        }

        post("/bulkDelete") {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val returns = call.receive<List<SaleReturn>>()
                service.deleteBatch(license, returns)
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
                val body = call.receiveText()
                val returns = Json.decodeFromString<List<SaleReturn>>(body)
                if (returns.isEmpty()) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, returns)
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(true, "Saved successfully")
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Error: ${e.message}")
                )
            }
        }
    }
}