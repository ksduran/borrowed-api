package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.TransferPayments
import com.kevinduran.domain.models.TransferPayment
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toTransferPayment() = TransferPayment(
    uuid = this[TransferPayments.uuid],
    license = this[TransferPayments.license],
    storeName = this[TransferPayments.storeName],
    imagePath = this[TransferPayments.imagePath],
    products = this[TransferPayments.products],
    imageSyncStatus = this[TransferPayments.imageSyncStatus],
    syncStatus = this[TransferPayments.syncStatus],
    deleted = this[TransferPayments.deleted],
    updatedAt = this[TransferPayments.updatedAt],
    createdAt = this[TransferPayments.createdAt]
)