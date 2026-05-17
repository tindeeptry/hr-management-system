package com.example.hrapp.domain.usercase

import com.example.hrapp.data.remote.model.NhanVienRequest
import com.example.hrapp.data.repository.EmployeeRepository
import javax.inject.Inject

class AddEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(request: NhanVienRequest) =
        employeeRepository.themNhanVien(request)
}