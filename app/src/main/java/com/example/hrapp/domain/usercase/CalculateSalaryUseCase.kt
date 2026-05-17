package com.example.hrapp.domain.usercase

import com.example.hrapp.data.repository.SalaryRepository
import javax.inject.Inject

class CalculateSalaryUseCase @Inject constructor(
    private val salaryRepository: SalaryRepository
) {
    suspend operator fun invoke(nhanVienId: Int) =
        salaryRepository.tinhLuong(nhanVienId)
}