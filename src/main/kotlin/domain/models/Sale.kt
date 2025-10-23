package com.kevinduran.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Sale(
    val uuid: String = "",
    val license: String = "",
    val productId: String = "",
    val companyName: String = "",
    val supplierId: String = "",
    val color: String = "",
    val size: String = "",
    val sizeR: String = "",
    val changedProductId: String = "",
    val changedSize: String = "",
    val changedProductColor: String = "",
    val salePrice: Int = 0,
    val purchasePrice: Int = 0,
    val raisedBy: String = "",
    val updatedBy: String = "",
    val paymentStatus: String = "",
    val image: String = "",
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val updatedAt: Long = 0,
    val createdAt: Long = 0
)
