package com.chareem.master.di

import com.chareem.core.coreModule
import com.chareem.master.Repository.GlobalRepositoryImpl
import com.chareem.master.Repository.globalRepository
import com.chareem.master.data.local.AppDatabase
import com.chareem.master.data.remote.ApiService
import com.chareem.master.feature.main.first.FirstVM
import com.chareem.master.feature.main.second.SecondVM
import com.chareem.master.feature.main.third.ThirdVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val databaseModule = module {
    single { AppDatabase.getInstance(get()) }
}

val vmModule = module {
    viewModel { FirstVM(get()) }
    viewModel { SecondVM(get()) }
    viewModel { ThirdVM(get()) }
}

val mapperModule = module {
    //single { UserMapper() }
}

val serviceModule = module {
    factory <ApiService> { get<Retrofit>().create(ApiService::class.java) }
}

val repositoriesModule = module {
    single <globalRepository>{ GlobalRepositoryImpl(get()) }
}

val mainModule = listOf(
    coreModule,
    databaseModule,
    vmModule,
    mapperModule,
    serviceModule,
    repositoriesModule
)