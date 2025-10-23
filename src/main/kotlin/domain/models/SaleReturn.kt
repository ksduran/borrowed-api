package com.kevinduran.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class SaleReturn(
    val uuid: String = "",
    val license: String = "",
    val storeName: String = "",
    val imagePath: String = "",
    val products: String = "",
    val imageSyncStatus: Int = 0,
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)