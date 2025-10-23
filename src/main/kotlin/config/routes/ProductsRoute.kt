package com.kevinduran.config.routes

import com.kevinduran.domain.models.Product
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.MessageSenderService
import com.kevinduran.infrastructure.services.ProductsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.productsRoutes(service: ProductsService) {

    route("/products") {
        get {
            val license = call.request.headers["X-License-Code"]!!
            val lastSync = call.request.queryParameters["lastSync"]?.toLong() ?: 0L
            val result = service.getBatch(license, lastSync)
            call.respond(result)
        }

        post {
            try {
                val license = call.request.headers["X-License-Code"]!!
                val products = call.receive<List<Product>>()
                if (products.isEmpty()) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, products)
                val data = mapOf("type" to "products")
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