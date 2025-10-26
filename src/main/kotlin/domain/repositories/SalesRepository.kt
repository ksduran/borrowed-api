package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.EarningsSummary
import com.kevinduran.domain.models.Sale
import com.kevinduran.domain.models.Statistics

interface SalesRepository {
    fun getToday(
        license: String,
        companyName: String,
        paymentStatus: String,
        raisedBy: String
    ): List<Sale>

    fun getByRange(
        license: String,
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): List<Sale>

    fun getBySupplier(
        license: String,
        supplierId: String,
        from: Long,
        to: Long
    ): List<Sale>

    fun getStatistics(
        license: String,
        productId: String,
        from: Long,
        to: Long
    ): Statistics

    fun getEarningsSummary(
        license: String,
        from: Long,
        to: Long
    ): EarningsSummary

    fun getTotalCashByUser(
        license: String,
        raisedBy: String
    ): Int

    fun deleteBatch(license: String, sales: List<Sale>)
    fun putBatch(license: String, sales: List<Sale>)
}