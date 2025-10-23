package com.kevinduran.config.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.time.Instant
import java.util.Date
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun Route.healthRoutes() {
    route("/health") {
        get {
            val millis = System.currentTimeMillis()
            val instant = Instant.ofEpochMilli(millis)
            val now = Date.from(instant).toString()
            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "status" to "ok",
                    "message" to "API operational",
                    "timestamp" to now
                )
            )
        }
    }
}