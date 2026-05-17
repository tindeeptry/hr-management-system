package com.example.hrapp.domain.usercase

import com.example.hrapp.data.repository.EmployeeRepository
import com.example.hrapp.data.repository.SalaryRepository
import javax.inject.Inject

class DeleteEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val salaryRepository: SalaryRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> {
        // Kiểm tra lương chưa thanh toán
        val luongResponse = salaryRepository.getLuongTheoNhanVien(id)
        if (luongResponse.success && luongResponse.data != null) {
            val conNoLuong = luongResponse.data.any {
                it.trangThai == "chua_thanh_toan"
            }
            if (conNoLuong) {
                return Result.failure(
                    Exception("Nhân viên còn lương chưa thanh toán, vui lòng thanh toán trước khi xóa!")
                )
            }
        }
        // Nếu đã thanh toán hết thì tiến hành xóa
        val xoaResponse = employeeRepository.xoaNhanVien(id)
        return if (xoaResponse.success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception(xoaResponse.message))
        }
    }
}