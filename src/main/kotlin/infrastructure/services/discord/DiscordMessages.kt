package com.kevinduran.infrastructure.services.discord

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant

object DiscordMessages {

    private fun getDiscordWebhook(config: ApplicationConfig): String {
        val discord = config.config("ktor.discord")
        return discord.property("web_hook").getString()
    }

    suspend fun notifyFailure(
        config: ApplicationConfig,
        error: Throwable
    ) {
        val webHook = getDiscordWebhook(config)
        val payload = """
        {
          "embeds": [
            {
              "title": "API Failure",
              "description": "A server exception was detected.",
              "color": 15548997,
              "fields": [
                { 
                    "name": "Error Message", 
                    "value": "${error.message ?: "Unknown"}",
                    "inline": true
                }
              ],
              "footer": { "text": "Ktor Backend • Bogotá Time" },
              "timestamp": "${Instant.now()}"
            }
          ]
        }
    """.trimIndent()

        withContext(Dispatchers.IO) {
            discordClient.post(webHook) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
        }
    }


    suspend fun notifyJobSuccess(
        config: ApplicationConfig,
        description: String,
        title: String = "Job Completed"
    ) {
        val webHook = getDiscordWebhook(config)
        val payload = """
        {
          "embeds": [
            {
              "title": "$title",
              "description": "$description",
              "color": 5763719,
              "footer": {
                "text": "Ktor Backend • Bogotá Time"
              },
              "timestamp": "${Instant.now()}"
            }
          ]
        }
    """.trimIndent()

        withContext(Dispatchers.IO) {
            discordClient.post(webHook) {
                contentType(ContentType.Application.Json)
                setBody(payload)
            }
        }
    }


}