package com.example.globarstest.di

import android.content.Context
import com.example.globarstest.data.GlobarsRepository
import com.example.globarstest.data.Repository
import com.example.globarstest.data.network.GlobarsService
import com.example.globarstest.data.network.NetworkService
import com.example.globarstest.ui.authorization.AuthorizationViewModel
import com.example.globarstest.ui.authorization.AuthorizationViewModelFactory
import com.example.globarstest.ui.map.MapViewModel
import com.example.globarstest.ui.map.MapViewModelFactory
import com.example.globarstest.util.ConnectionStatus
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideAuthorizationViewModel(
        repository: Repository,
        connectionStatus: ConnectionStatus
    ) = AuthorizationViewModelFactory(
        repository,
        connectionStatus
    ).create(AuthorizationViewModel::class.java)

    @Singleton
    @Provides
    fun provideMapViewModel(
        repository: Repository,
        connectionStatus: ConnectionStatus
    ) = MapViewModelFactory(repository, connectionStatus).create(MapViewModel::class.java)

    @Singleton
    @Provides
    fun provideRepository(repository: GlobarsRepository): Repository = repository

    @Singleton
    @Provides
    fun provideConnectionStatus(): ConnectionStatus = ConnectionStatus(context)

    @Singleton
    @Provides
    fun provideNetworkService(networkService: GlobarsService): NetworkService =
        networkService

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://test.globars.ru/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(60, TimeUnit.SECONDS).build()
}