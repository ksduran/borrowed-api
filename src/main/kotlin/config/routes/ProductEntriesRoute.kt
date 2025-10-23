package com.kevinduran.config.routes

import com.kevinduran.domain.models.ProductEntry
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.MessageSenderService
import com.kevinduran.infrastructure.services.ProductEntriesService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.productEntriesRoute(service: ProductEntriesService) {
    route("/product-entries") {

        get {
            val license = call.request.headers["X-License-Code"]!!
            val lastSync = call.request.queryParameters["last-sync"]?.toLong() ?: 0L
            val result = service.getBatch(license, lastSync)
            call.respond(result)
        }

        post {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val entries = call.receive<List<ProductEntry>>()
                service.putBatch(license, entries)
                val data = mapOf("type" to "productEntries")
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