package com.kevinduran.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class EarningsSummary(
    val earnings: Int,
    val totalSold: Int
)