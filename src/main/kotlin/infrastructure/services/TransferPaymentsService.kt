package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.TransferPayment
import com.kevinduran.domain.repositories.TransferPaymentsRepository

class TransferPaymentsService(private val repository: TransferPaymentsRepository) {

    fun getBatch(license: String, lastSync: Long): List<TransferPayment> =
        repository.getBatch(license, lastSync)

    fun putBatch(license: String, payments: List<TransferPayment>) =
        repository.putBatch(license, payments)

}