package com.kevinduran.infrastructure.services

import com.kevinduran.domain.models.Employee
import com.kevinduran.domain.repositories.EmployeesRepository

class EmployeesService(private val repository: EmployeesRepository) {

    fun getBatch(license: String): List<Employee> =
        repository.getBatch(license)

    fun putBatch(license: String, employees: List<Employee>) =
        repository.putBatch(license, employees)

    fun deleteBatch(license: String, employees: List<Employee>) =
        repository.deleteBatch(license, employees)

}