package com.example.hrapp.domain.usercase

import com.example.hrapp.data.repository.EmployeeRepository
import javax.inject.Inject

class GetEmployeeListUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke() =
        employeeRepository.getDanhSachNhanVien()
}