package com.guri.guridocpat.common.di

import com.guri.guridocpat.doctordashboard.domain.DoctorRepository
import com.guri.guridocpat.doctordashboard.domain.DoctorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindDoctorRepository(
        doctorRepositoryImpl: DoctorRepositoryImpl
    ): DoctorRepository
}