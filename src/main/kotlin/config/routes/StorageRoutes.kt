package com.kevinduran.config.routes

import com.kevinduran.infrastructure.reponse.ApiResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
            status = HttpStatusCode.BadRequest,
            ApiResponse(false, "Error: ${e.message}")
        )
    }
}
