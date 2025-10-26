package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.Sales
import com.kevinduran.domain.models.EarningsSummary
import com.kevinduran.domain.models.Sale
import com.kevinduran.domain.models.Statistics
import com.kevinduran.domain.repositories.SalesRepository
import com.kevinduran.infrastructure.mappers.toSale
import org.jetbrains.exposed.v1.core.Case
import org.jetbrains.exposed.v1.core.IntegerColumnType
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.Sum
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.intLiteral
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.minus
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.time.LocalDate
import java.time.ZoneId

class SalesRepositoryImpl : SalesRepository {
    override fun getToday(
        license: String,
        companyName: String,
        paymentStatus: String,
        raisedBy: String
    ): List<Sale> {
        return transaction {

            val zone = ZoneId.of("America/Bogota")

            val startOfDay = LocalDate.now(zone)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()

            val endOfDay = LocalDate.now(zone)
                .plusDays(1)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli() - 1

            Sales.selectAll().where {
                (Sales.paymentStatus like "%$paymentStatus%") and
                        (Sales.companyName like "%$companyName%") and
                        (Sales.raisedBy like "%$raisedBy%") and
                        (Sales.updatedAt.between(startOfDay, endOfDay)) and
                        (Sales.license eq license)
            }
                .orderBy(Sales.createdAt, SortOrder.DESC)
                .map { it.toSale() }
        }
    }

    override fun getByRange(
        license: String,
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): List<Sale> {
        return transaction {
            Sales.selectAll().where {
                (Sales.paymentStatus like "%$paymentStatus%") and
                        (Sales.companyName like "%$companyName%") and
                        (Sales.raisedBy like "%$raisedBy%") and
                        (Sales.createdAt.between(from, to)) and
                        (Sales.license eq license)
            }
                .orderBy(Sales.createdAt, SortOrder.DESC)
                .map { it.toSale() }
        }
    }

    override fun getBySupplier(
        license: String,
        supplierId: String,
        from: Long,
        to: Long
    ): List<Sale> {
        return transaction {
            Sales.selectAll().where {
                (Sales.license eq license) and
                        (Sales.supplierId eq supplierId) and
                        (Sales.updatedAt.between(from, to))
            }.orderBy(Sales.companyName, SortOrder.ASC)
                .map { it.toSale() }
        }
    }

    override fun getStatistics(
        license: String,
        productId: String,
        from: Long,
        to: Long
    ): Statistics {
        return transaction {
            val totalRecorded = Sales.uuid.count()
            val totalPending = Sum(
                Case()
                    .When(Sales.paymentStatus eq "Pendiente", intLiteral(1))
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )
            val totalReturn = Sum(
                Case()
                    .When(Sales.paymentStatus eq "Devolución", intLiteral(0))
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )
            val totalChanges = Sum(
                Case()
                    .When(Sales.paymentStatus eq "Cambio", intLiteral(1))
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )
            val totalPaidByTransfer = Sum(
                Case()
                    .When(Sales.paymentStatus eq "Pago Transferencia", intLiteral(1))
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )
            val totalPaidByCash = Sum(
                Case()
                    .When(Sales.paymentStatus eq "Pago Efectivo", intLiteral(1))
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )
            val totalByRaised = Sum(
                Case()
                    .When(Sales.paymentStatus eq "Por cobrar", intLiteral(1))
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )
            val totalSold = Sum(
                Case()
                    .When(
                        Sales.paymentStatus like "Pago%",
                        Sales.salePrice minus Sales.purchasePrice
                    )
                    .Else(intLiteral(0)),
                columnType = IntegerColumnType()
            )

            val row = Sales.select(
                totalRecorded,
                totalPending,
                totalReturn,
                totalChanges,
                totalPaidByTransfer,
                totalPaidByCash,
                totalByRaised,
                totalSold
            ).where {
                (Sales.productId eq productId) and
                        (Sales.updatedAt.between(from, to)) and
                        (Sales.license eq license)
            }.single()

            Statistics(
                totalRecorded = row[totalRecorded].toInt(),
                totalPending = row[totalPending]?.toInt() ?: 0,
                totalReturn = row[totalReturn]?.toInt() ?: 0,
                totalChanges = row[totalChanges]?.toInt() ?: 0,
                totalPaidByTransfer = row[totalPaidByTransfer]?.toInt() ?: 0,
                totalPaidByCash = row[totalPaidByCash]?.toInt() ?: 0,
                totalByRaised = row[totalByRaised]?.toInt() ?: 0,
                totalSold = row[totalSold]?.toInt() ?: 0
            )
        }
    }

    override fun getEarningsSummary(
        license: String,
        from: Long,
        to: Long
    ): EarningsSummary {
        return transaction {

            val earnings = Sum(
                Case()
                    .When(
                        (Sales.paymentStatus like "Pago%") and (Sales.salePrice greater 0),
                        Sales.salePrice minus Sales.purchasePrice
                    )
                    .Else(intLiteral(0)),
                IntegerColumnType()
            )

            val totalSold = Sum(
                Case()
                    .When(
                        (Sales.paymentStatus like "Pago%") and (Sales.salePrice greater 0),
                        Sales.salePrice
                    )
                    .Else(intLiteral(0)),
                IntegerColumnType()
            )

            Sales.select(
                earnings,
                totalSold
            ).where {
                (Sales.license eq license) and (Sales.updatedAt.between(from, to))
            }.single().let {
                EarningsSummary(
                    earnings = it[earnings] ?: 0,
                    totalSold = it[totalSold] ?: 0
                )
            }
        }
    }

    override fun getTotalCashByUser(
        license: String,
        raisedBy: String
    ): Int {
        return transaction {
            val zone = ZoneId.of("America/Bogota")

            val startOfDay = LocalDate.now(zone)
                .atStartOfDay(zone)
                .toInstant().toEpochMilli()
            val endOfDay = LocalDate.now(zone)
                .plusDays(1)
                .atStartOfDay(zone)
                .toInstant().toEpochMilli() - 1

            val totalCash = Sum(
                Case()
                    .When(
                        (Sales.paymentStatus eq "Pago Efectivo"),
                        Sales.salePrice
                    ).Else(intLiteral(0)),
                IntegerColumnType()
            )

            Sales.select(totalCash).where {
                (Sales.license eq license) and
                        (Sales.raisedBy eq raisedBy) and
                        (Sales.updatedAt.between(startOfDay, endOfDay))
            }.single().let {
                it[totalCash] ?: 0
            }
        }
    }


    override fun putBatch(
        license: String,
        sales: List<Sale>
    ) {
        transaction {
            Sales.batchUpsert(
                data = sales,
                onUpdateExclude = listOf(Sales.uuid, Sales.createdAt),
                body = { sale ->
                    this[Sales.uuid] = sale.uuid
                    this[Sales.license] = sale.license
                    this[Sales.productId] = sale.productId
                    this[Sales.supplierId] = sale.supplierId
                    this[Sales.changedProductId] = sale.changedProductId
                    this[Sales.companyName] = sale.companyName
                    this[Sales.color] = sale.color
                    this[Sales.size] = sale.size
                    this[Sales.sizeR] = sale.sizeR
                    this[Sales.changedSize] = sale.changedSize
                    this[Sales.changedProductColor] = sale.changedProductColor
                    this[Sales.salePrice] = sale.salePrice
                    this[Sales.purchasePrice] = sale.purchasePrice
                    this[Sales.raisedBy] = sale.raisedBy
                    this[Sales.updatedBy] = sale.updatedBy
                    this[Sales.paymentStatus] = sale.paymentStatus
                    this[Sales.image] = sale.image
                    this[Sales.syncStatus] = sale.syncStatus
                    this[Sales.deleted] = sale.deleted
                    this[Sales.updatedAt] = sale.updatedAt
                    this[Sales.createdAt] = sale.createdAt
                }
            )
        }
    }

    override fun deleteBatch(license: String, sales: List<Sale>) {
        if (sales.isEmpty()) return
        transaction {
            val uuids = sales.map { it.uuid }
            Sales.deleteWhere { Sales.uuid inList uuids }
        }
    }
}