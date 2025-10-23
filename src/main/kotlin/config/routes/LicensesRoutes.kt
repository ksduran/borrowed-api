package com.kevinduran.config.routes

import com.kevinduran.domain.models.License
import com.kevinduran.infrastructure.reponse.ApiResponse
import com.kevinduran.infrastructure.services.LicenseService
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.licensesRoutes(licenseService: LicenseService) {

    route("/licenses") {
        get {
            val packageName = call.request.headers["X-Package-Name"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse(false, "Package is required")
                )
            val isTest = call.request.queryParameters["isTest"]?.toBoolean() ?: false
            val license = licenseService.findLicense(packageName, isTest)
            if (license == null) {
                call.respond(
                    ApiResponse(false, "License not found")
                )
            } else {
                call.respond(license)
            }
        }

        post {
            try {
                val request = call.receive<License>()
                val license = licenseService.add(request)
                call.respond(license)
            } catch (e: Exception) {
                call.respond(
                    ApiResponse(false, "Failed to create license, error: ${e.message}")
                )
            }
        }

    }

}