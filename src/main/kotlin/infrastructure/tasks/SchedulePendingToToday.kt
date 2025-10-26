package com.kevinduran.infrastructure.tasks

import com.kevinduran.config.database.tables.Sales
import io.ktor.server.application.Application
import io.ktor.server.application.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

fun Application.schedulePendingToToday() {
    launch {
        val zone = ZoneId.of("America/Bogota")

        while (true) {
            try {
                val now = ZonedDateTime.now(zone)
                val nextMidnight = now.toLocalDate()
                    .plusDays(1)
                    .atStartOfDay(zone)

                val millisToMidnight = Duration.between(now, nextMidnight).toMillis()
                log.info("Pending job schedule in ${millisToMidnight / 1000 / 60} minutes...")
                delay(millisToMidnight)

                val startOfDay = nextMidnight.toInstant().toEpochMilli()
                val endOfPrevDay = startOfDay - 1
                val nowMillis = Instant.now().toEpochMilli()

                val updatedRows = transaction {
                    Sales.update({
                        (Sales.paymentStatus inList listOf("Pendiente", "Por cobrar")) and
                                (Sales.updatedAt less endOfPrevDay)
                    }) {
                        it[paymentStatus] = "Por cobrar"
                        it[updatedAt] = nowMillis
                        it[updatedBy] = "Server-VPS (Ubuntu)"
                    }
                }

                log.info("Schedule job finished. Affected rows: $updatedRows")
                delay(24 * 60 * 60 * 1000L)

            } catch (e: Exception) {
                log.error("Failed pending to today job", e)
                delay(60_000L)
            }
        }
    }
}