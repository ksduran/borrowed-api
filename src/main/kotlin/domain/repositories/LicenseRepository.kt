package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.License

interface LicenseRepository {
    suspend fun findLicense(packageName: String, isTest: Boolean): License?
    suspend fun add(license: License): License
    suspend fun validateLicense(code: String, packageName: String): Boolean
}