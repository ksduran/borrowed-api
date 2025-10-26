package com.kevinduran.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Supplier(
    val uuid: String,
    val license: String,
    val name: String,
    val syncStatus: Int,
    val deleted: Int,
    val raisedBy: String,
    val updatedBy: String,
    val updatedAt: Long,
    val createdAt: Long
)
