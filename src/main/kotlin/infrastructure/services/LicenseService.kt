package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.License
import com.kevinduran.domain.repositories.LicenseRepository

class LicenseService(private val repository: LicenseRepository) {

    suspend fun findLicense(packageName: String, isTest: Boolean): License? {
        val license = repository.findLicense(
            packageName = packageName,
            isTest = isTest
        )
        return license?.let {
            License(
                id = it.id,
                code = it.code,
                active = it.active,
                packageName = it.packageName
            )
        }
    }

    suspend fun add(license: License): License {
        val license = repository.add(license)
        return License(
            id = license.id,
            code = license.code,
            active = license.active,
            packageName = license.packageName
        )
    }

    suspend fun validateLicense(
        code: String,
        packageName: String
    ): Boolean = repository.validateLicense(code, packageName)


}