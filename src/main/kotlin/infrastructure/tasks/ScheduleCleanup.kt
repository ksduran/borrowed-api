
package com.kevinduran.infrastructure.tasks

import com.kevinduran.config.database.tables.SaleReturns
import com.kevinduran.config.database.tables.Sales
import com.kevinduran.config.database.tables.TransferPayments
import io.ktor.server.application.Application
import io.ktor.server.application.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.notLike
import com.kevinduran.infrastructure.mappers.toSaleReturn
import com.kevinduran.infrastructure.mappers.toTransferPayment
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

fun Application.scheduleCleanup() {
    launch {
        val zone = ZoneId.of("America/Bogota")

        while (true) {
            try {
                val now = ZonedDateTime.now(zone)
                val nextMidnight = now.toLocalDate()
                    .plusDays(1)
                    .atStartOfDay(zone)

                val millisToMidnight = Duration.between(now, nextMidnight).toMillis()
                log.info("Cleanup job schedule in ${millisToMidnight / 1000 / 60} minutes...")
                delay(millisToMidnight)

                val fifteenDaysAgo = ZonedDateTime.now(zone).minusDays(15).toInstant().toEpochMilli()
                val sixtyDaysAgo = ZonedDateTime.now(zone).minusDays(60).toInstant().toEpochMilli()

                val transferPaymentsToDelete = transaction {
                    TransferPayments.selectAll().where { TransferPayments.createdAt less fifteenDaysAgo }.map { it.toTransferPayment() }
                }

                transferPaymentsToDelete.forEach { payment ->
                    if (payment.imagePath.isNotBlank()) {
                        val file = File("/var/duran-service/borrowed/${payment.license}/payments/", payment.imagePath)
                        if (file.exists()) {
                            file.delete()
                        }
                    }
                }

                val deletedTransferPayments = transaction {
                    TransferPayments.deleteWhere { TransferPayments.createdAt less fifteenDaysAgo }
                }

                val saleReturnsToDelete = transaction {
                    SaleReturns.selectAll().where { SaleReturns.createdAt less fifteenDaysAgo }.map { it.toSaleReturn() }
                }

                saleReturnsToDelete.forEach { saleReturn ->
                    if (saleReturn.imagePath.isNotBlank()) {
                        val file = File("/var/duran-service/borrowed/${saleReturn.license}/returns/", saleReturn.imagePath)
                        if (file.exists()) {
                            file.delete()
                        }
                    }
                }

                val deletedSaleReturns = transaction {
                    SaleReturns.deleteWhere { SaleReturns.createdAt less fifteenDaysAgo }
                }

                val deletedSales = transaction {
                    Sales.deleteWhere {
                        (Sales.paymentStatus notLike "Pago%") and
                                (Sales.updatedAt less sixtyDaysAgo)
                    }
                }

                log.info("Cleanup job finished. Deleted TransferPayments: $deletedTransferPayments, SaleReturns: $deletedSaleReturns, Sales: $deletedSales")
                delay(24 * 60 * 60 * 1000L)

            } catch (e: Exception) {
                log.error("Failed to run cleanup job", e)
                delay(60_000L)
            }
        }
    }
}
