package com.kevinduran.infrastructure.mappers

import com.kevinduran.config.database.tables.Licenses
import com.kevinduran.domain.models.License
import org.jetbrains.exposed.v1.core.ResultRow


fun ResultRow.toLicense() = License(
    id = this[Licenses.id],
    code = this[Licenses.code],
    active = this[Licenses.active],
    packageName = this[Licenses.packageName]
)