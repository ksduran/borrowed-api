package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.TransferPayment
import com.kevinduran.domain.repositories.TransferPaymentsRepository

class TransferPaymentsService(private val repository: TransferPaymentsRepository) {

    fun getBatch(license: String): List<TransferPayment> =
        repository.getBatch(license)

    fun putBatch(license: String, payments: List<TransferPayment>) =
        repository.putBatch(license, payments)

    fun deleteBatch(license: String, payments: List<TransferPayment>) =
        repository.deleteBatch(license, payments)

}