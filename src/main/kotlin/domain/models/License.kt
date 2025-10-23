package com.kevinduran.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val id: Int = 0,
    val code: String = "",
    val active: Int = 0,
    val packageName: String = ""
)
