package com.kevinduran.infrastructure.tasks

import com.kevinduran.config.database.tables.Sales
import com.kevinduran.infrastructure.services.discord.DiscordMessages
import io.ktor.server.application.Application
import io.ktor.server.application.log
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun Application.schedulePendingToToday(config: ApplicationConfig) {
    launch {
        val zone = ZoneId.of("America/Bogota")

        while (true) {
            try {
                val now = ZonedDateTime.now(zone)
                val nextMidnight = now.toLocalDate()
                    .plusDays(1)
                    .atStartOfDay(zone)

                val millisToMidnight = Duration.between(now, nextMidnight).toMillis()
                log.info("Pending job schedule in ${millisToMidnight / 1000 / 60} minutes")
                DiscordMessages.notifyJobSuccess(
                    title = "To Today Task",
                    config = config,
                    description = "Pending job schedule in ${millisToMidnight / 1000 / 60} minutes"
                )

                delay(millisToMidnight)

                val startOfDay = nextMidnight.toInstant().toEpochMilli()
                val endOfPrevDay = startOfDay - 1
                val nowMillis = Instant.now().toEpochMilli()

                val updatedRows = withContext(Dispatchers.IO) {
                    transaction {
                        Sales.update({
                            (Sales.paymentStatus inList listOf("Pendiente", "Por cobrar")) and
                                    (Sales.updatedAt less endOfPrevDay)
                        }) {
                            it[paymentStatus] = "Por cobrar"
                            it[updatedAt] = nowMillis
                            it[updatedBy] = "Server-VPS (Ubuntu)"
                        }
                    }
                }

                DiscordMessages.notifyJobSuccess(
                    title = "To Today Task",
                    config = config,
                    description = "Schedule job completed.\n\n Affected: $updatedRows"
                )
            } catch (e: Exception) {
                DiscordMessages.notifyFailure(
                    config = config,
                    error = e
                )
                delay(60_000L)
            }
        }
    }
}