package com.kevinduran.infrastructure.plugins

import com.kevinduran.infrastructure.services.LicenseService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.PipelineCall
import io.ktor.server.application.call
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

class ValidationInterceptor(private val licenseService: LicenseService) {

    suspend fun intercept(context: PipelineContext<Unit, PipelineCall>) {
        val call = context.call
        val path = call.request.path()
        println(path)

        val excludedPaths = listOf("/health", "/licenses", "/files")
        if (excludedPaths.any { excluded ->
                path == excluded || path.startsWith("${excluded}/")
            }) return

        val licenseCode = call.request.headers["X-License-Code"]
        val packageName = call.request.headers["X-Package-Name"]

        if (licenseCode == null || packageName == null) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("Denied" to "You do not have privileges to view this information")
            )
            context.finish()
            return
        }

        val validation = licenseService.validateLicense(
            code = licenseCode,
            packageName = packageName
        )
        if (!validation) {
            call.respond(
                HttpStatusCode.Forbidden,
                mapOf(
                    "Denied" to "Invalid credentials"
                )
            )
            context.finish()
            return
        }
    }

}

fun Application.configureValidation(licenseService: LicenseService) {
    val interceptor = ValidationInterceptor(licenseService)

    intercept(ApplicationCallPipeline.Plugins) {
        interceptor.intercept(this)
    }
}