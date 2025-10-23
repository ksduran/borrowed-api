package com.kevinduran.config.routes

import com.kevinduran.domain.models.SupplierData
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.MessageSenderService
import com.kevinduran.infrastructure.services.SuppliersDataService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json

fun Route.suppliersDataRoute(service: SuppliersDataService) {
    route("/suppliers-data") {
        get {
            val license = call.request.headers["X-License-Code"]!!
            val lastSync = call.request.queryParameters["lastSync"]?.toLong() ?: 0L
            val result = service.getBatch(license, lastSync)
            call.respond(result)
        }

        post {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val body = call.receiveText()
                val data = Json.decodeFromString<List<SupplierData>>(body)
                if (data.isEmpty()) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, data)
                val msg = mapOf("type" to "suppliers")
                MessageSenderService.sendToTopic(license, msg)
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