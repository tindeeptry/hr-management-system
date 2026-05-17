package com.example.hrapp.di

import com.example.hrapp.data.remote.AuthInterceptor
import com.example.hrapp.data.remote.RetrofitClient
import com.example.hrapp.data.remote.api.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        RetrofitClient.provideOkHttpClient(authInterceptor)

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        RetrofitClient.provideRetrofit(okHttpClient)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        RetrofitClient.provideAuthApi(retrofit)

    @Provides
    @Singleton
    fun provideEmployeeApi(retrofit: Retrofit): EmployeeApi =
        RetrofitClient.provideEmployeeApi(retrofit)

    @Provides
    @Singleton
    fun provideDepartmentApi(retrofit: Retrofit): DepartmentApi =
        RetrofitClient.provideDepartmentApi(retrofit)

    @Provides
    @Singleton
    fun provideAttendanceApi(retrofit: Retrofit): AttendanceApi =
        RetrofitClient.provideAttendanceApi(retrofit)

    @Provides
    @Singleton
    fun provideSalaryApi(retrofit: Retrofit): SalaryApi =
        RetrofitClient.provideSalaryApi(retrofit)
}