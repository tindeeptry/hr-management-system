package com.example.hrapp.domain.usercase

import com.example.hrapp.data.remote.model.TamUngRequest
import com.example.hrapp.data.repository.SalaryRepository
import javax.inject.Inject

class AdvanceSalaryUseCase @Inject constructor(
    private val salaryRepository: SalaryRepository
) {
    suspend operator fun invoke(nhanVienId: Int, soTien: Double, lyDo: String?) =
        salaryRepository.ungLuong(TamUngRequest(nhanVienId, soTien, lyDo))
}
