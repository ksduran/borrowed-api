package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.TransferPayment

interface TransferPaymentsRepository {
    fun getBatch(license: String, lastSync: Long): List<TransferPayment>
    fun putBatch(license: String, payments: List<TransferPayment>)
}