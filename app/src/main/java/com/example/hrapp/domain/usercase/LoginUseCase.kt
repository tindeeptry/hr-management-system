package com.example.hrapp.domain.usercase

import com.example.hrapp.data.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, matKhau: String) =
        authRepository.login(email, matKhau)
}