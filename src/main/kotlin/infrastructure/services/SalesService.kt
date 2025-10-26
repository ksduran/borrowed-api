package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.EarningsSummary
import com.kevinduran.domain.models.Sale
import com.kevinduran.domain.models.Statistics
import com.kevinduran.domain.repositories.SalesRepository

class SalesService(private val repository: SalesRepository) {

    fun getToday(
        license: String,
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
    ): List<Sale> =
        repository.getToday(
            license = license,
            companyName = companyName,
            paymentStatus = paymentStatus,
            raisedBy = raisedBy
        )

    fun getByRange(
        license: String,
        companyName: String,
        paymentStatus: String,
        raisedBy: String,
        from: Long,
        to: Long
    ): List<Sale> = repository.getByRange(
        license = license,
        companyName = companyName,
        paymentStatus = paymentStatus,
        raisedBy = raisedBy,
        from = from,
        to = to
    )

    fun getBySupplier(
        license: String,
        supplierId: String,
        from: Long,
        to: Long
    ): List<Sale> = repository.getBySupplier(
        license = license,
        supplierId = supplierId,
        from = from,
        to = to
    )

    fun getStatistics(
        license: String,
        productId: String,
        from: Long,
        to: Long
    ): Statistics = repository.getStatistics(
        license = license,
        productId = productId,
        from = from,
        to = to
    )

    fun getEarningsSummary(
        license: String,
        from: Long,
        to: Long
    ): EarningsSummary = repository.getEarningsSummary(
        license = license,
        from = from,
        to = to
    )

    fun getTotalCashByUser(
        license: String,
        raisedBy: String
    ): Int = repository.getTotalCashByUser(
        license = license,
        raisedBy = raisedBy
    )

    fun putBatch(
        license: String,
        sales: List<Sale>
    ) = repository.putBatch(
        license = license,
        sales = sales
    )

    fun deleteBatch(
        license: String,
        sales: List<Sale>
    ) = repository.deleteBatch(
        license = license,
        sales = sales
    )

}