package com.kevinduran.config.routes

import com.kevinduran.domain.models.TransferPayment
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.MessageSenderService
import com.kevinduran.infrastructure.services.TransferPaymentsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json

fun Route.transferPaymentsRoute(service: TransferPaymentsService) {
    route("/transfer-payments") {
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
                val payments = Json.decodeFromString<List<TransferPayment>>(body)
                if (payments.isEmpty()) {
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, payments)
                val data = mapOf("type" to "transferPayments")
                MessageSenderService.sendToTopic(license, data)
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