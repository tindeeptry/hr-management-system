package com.example.hrapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hrapp.domain.usercase.LoginUseCase
import com.example.hrapp.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, matKhau: String) {
        if (email.isBlank() || matKhau.isBlank()) {
            _uiState.value = LoginUiState.Error("Vui lòng nhập đầy đủ thông tin!")
            return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val response = loginUseCase(email, matKhau)
                if (response.success && response.data != null) {
                    _uiState.value = LoginUiState.Success(response.data.vaiTro)
                } else {
                    _uiState.value = LoginUiState.Error(response.message)
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Không thể kết nối đến máy chủ!")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    // Kiểm tra token còn hiệu lực không (tự động đăng nhập)
    fun checkLoginState() {
        viewModelScope.launch {
            val token = tokenManager.token.firstOrNull()
            val role = tokenManager.role.firstOrNull()
            if (!token.isNullOrEmpty() && !role.isNullOrEmpty()) {
                _uiState.value = LoginUiState.Success(role)
            }
        }
    }
}

sealed class LoginUiState {
    object Idle    : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val vaiTro: String) : LoginUiState()
    data class Error(val message: String)  : LoginUiState()
}