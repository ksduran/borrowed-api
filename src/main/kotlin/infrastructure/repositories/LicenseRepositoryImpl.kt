package com.kevinduran.infrastructure.repositories

import com.kevinduran.config.database.tables.Licenses
import com.kevinduran.domain.models.License
import com.kevinduran.domain.repositories.LicenseRepository
import com.kevinduran.infrastructure.mappers.toLicense
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class LicenseRepositoryImpl : LicenseRepository {
    override suspend fun findLicense(
        packageName: String,
        isTest: Boolean
    ): License? {
        return transaction {
            if (isTest) {
                License(
                    code = "7001234567",
                    packageName = "Package-Test",
                    active = 1
                )
            } else {
                Licenses.select(Licenses.columns)
                    .where { Licenses.packageName eq packageName }
                    .map { it.toLicense() }
                    .firstOrNull()
            }
        }
    }

    override suspend fun add(license: License): License {
        return transaction {
            val id = Licenses.insert {
                it[code] = license.code
                it[packageName] = license.packageName
                it[active] = license.active
            } get Licenses.id

            License(
                id = id,
                code = license.code,
                active = license.active,
                packageName = license.packageName
            )
        }
    }

    override suspend fun validateLicense(
        code: String,
        packageName: String
    ): Boolean {
        return transaction {
            val license = Licenses.select(Licenses.columns)
                .where { Licenses.code eq code }
                .map { it.toLicense() }
                .firstOrNull()

            return@transaction license != null
        }
    }

}