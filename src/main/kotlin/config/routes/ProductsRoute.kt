package com.kevinduran.config.routes

import com.kevinduran.domain.models.Product
import com.kevinduran.infrastructure.reponse.ApiResponse
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
                val products = call.receive<List<Product>>()
                service.deleteBatch(license, products)
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
                val products = call.receive<List<Product>>()
                if (products.isEmpty()) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(true, "Nothing to save")
                    )
                    return@post
                }
                service.putBatch(license, products)
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