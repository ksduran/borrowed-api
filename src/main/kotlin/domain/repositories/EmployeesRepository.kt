package com.kevinduran.domain.repositories

import com.kevinduran.domain.models.Employee

interface EmployeesRepository {
    fun getBatch(license: String, lastSync: Long): List<Employee>
    fun putBatch(license: String, employees: List<Employee>)
    fun deleteBatch(license: String, employees: List<Employee>)
}