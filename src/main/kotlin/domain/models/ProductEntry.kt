package com.kevinduran.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductEntry(
    val uuid: String = "",
    val license: String = "",
    val productId: String = "",
    val image: String = "",
    val syncStatus: Int = 0,
    val deleted: Int = 0,
    val raisedBy: String = "",
    val updatedBy: String = "",
    val updatedAt: Long = 0L,
    val createdAt: Long = 0L
)