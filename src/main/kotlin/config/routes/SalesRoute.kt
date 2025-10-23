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
            val lastSync = call.request.queryParameters["lastSync"]?.toLong() ?: 0L
            val result = service.getBatch(license, lastSync)
            call.respond(result)
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