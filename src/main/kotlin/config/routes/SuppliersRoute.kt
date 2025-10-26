package com.kevinduran.config.routes

import com.kevinduran.domain.models.Supplier
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.SuppliersService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.suppliersRoutes(service: SuppliersService) {

    route("/suppliers") {
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
                val suppliers = call.receive<List<Supplier>>()
                service.deleteBatch(license, suppliers)
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
                val suppliers = call.receive<List<Supplier>>()
                if (suppliers.isEmpty()) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, suppliers)
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
