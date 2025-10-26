package com.kevinduran.config.routes

import com.kevinduran.infrastructure.reponse.ApiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingContext
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import java.io.File

fun Route.storageRoutes() {

    route("/storage") {

        post("/upload-product") {
            uploadImageByType(
                folderName = "products",
                successMessage = "Product image uploaded successfully"
            )
        }

        post("/upload-return") {
            uploadImageByType(
                folderName = "returns",
                successMessage = "Return image uploaded successfully"
            )
        }

        post("/upload-payment") {
            uploadImageByType(
                folderName = "payments",
                successMessage = "Payment image uploaded successfully"
            )
        }
    }
}

private suspend fun RoutingContext.uploadImageByType(
    folderName: String,
    successMessage: String
) {
    try {
        val license = call.request.headers["X-License-Code"]!!
        val multipart = call.receiveMultipart()
        var fileName: String? = null
        var imageBytes: ByteArray? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "fileName") {
                        fileName = part.value
                    }
                }

                is PartData.FileItem -> {
                    imageBytes = part.streamProvider().readBytes()
                }

                else -> {}
            }
            part.dispose()
        }

        if (fileName.isNullOrBlank() || imageBytes == null) {
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse(false, "Missing data")
            )
            return
        }

        val baseDir = File("/var/duran-service/borrowed/$license/$folderName")
        baseDir.mkdirs()

        val file = File(baseDir, fileName)
        file.writeBytes(imageBytes)

        call.respond(
            HttpStatusCode.OK,
            ApiResponse(true, successMessage)
        )
    } catch (e: Exception) {
        call.respond(
            ApiResponse(false, "Error: ${e.message}")
        )
    }
}
