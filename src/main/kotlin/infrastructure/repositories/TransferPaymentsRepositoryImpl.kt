package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.TransferPayments
import com.kevinduran.domain.models.TransferPayment
import com.kevinduran.domain.repositories.TransferPaymentsRepository
import com.kevinduran.infrastructure.mappers.toTransferPayment
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File

class TransferPaymentsRepositoryImpl : TransferPaymentsRepository {
    override fun getBatch(
        license: String
    ): List<TransferPayment> {
        return transaction {
            TransferPayments.selectAll().where {
                (TransferPayments.license eq license)
            }
                .orderBy(TransferPayments.createdAt, SortOrder.DESC)
                .map { it.toTransferPayment() }
        }
    }

    override fun putBatch(
        license: String,
        payments: List<TransferPayment>
    ) {
        transaction {
            TransferPayments.batchUpsert(
                data = payments,
                onUpdateExclude = listOf(TransferPayments.uuid, TransferPayments.createdAt),
                body = { payment ->
                    this[TransferPayments.uuid] = payment.uuid
                    this[TransferPayments.license] = payment.license
                    this[TransferPayments.storeName] = payment.storeName
                    this[TransferPayments.imagePath] = payment.imagePath
                    this[TransferPayments.products] = payment.products
                    this[TransferPayments.imageSyncStatus] = payment.imageSyncStatus
                    this[TransferPayments.syncStatus] = payment.syncStatus
                    this[TransferPayments.deleted] = payment.deleted
                    this[TransferPayments.updatedAt] = payment.updatedAt
                    this[TransferPayments.createdAt] = payment.createdAt
                }
            )
        }
    }

    override fun deleteBatch(license: String, payments: List<TransferPayment>) {
        if (payments.isEmpty()) return
        transaction {
            payments.forEach { payment ->
                if (payment.imagePath.isNotBlank()) {
                    val file =
                        File("/var/duran-service/borrowed/$license/payments/", payment.imagePath)
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }

            val uuids = payments.map { it.uuid }
            TransferPayments.deleteWhere { TransferPayments.uuid inList uuids }
        }
    }

}