package com.akerimtay.rickandmorty.core.di

import android.content.Context
import com.akerimtay.rickandmorty.BuildConfig
import com.akerimtay.rickandmorty.character.data.remote.adapter.CharacterStatusTypeAdapter
import com.akerimtay.rickandmorty.character.data.remote.adapter.GenderTypeAdapter
import com.akerimtay.rickandmorty.character.domain.model.CharacterStatus
import com.akerimtay.rickandmorty.character.domain.model.Gender
import com.akerimtay.rickandmorty.core.network.DateTypeAdapter
import com.akerimtay.rickandmorty.core.network.NetworkResponseAdapterFactory
import com.akerimtay.rickandmorty.core.presentation.util.extensions.applyIf
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val DEFAULT_CONNECT_TIMEOUT = 15L
    private const val DEFAULT_READ_TIMEOUT = 15L
    private const val DEFAULT_WRITE_TIMEOUT = 15L
    private const val DEFAULT_PING_INTERVAL = 15L
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().applyIf(BuildConfig.DEBUG) { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor =
        ChuckerInterceptor.Builder(context = context)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateTypeAdapter())
            .registerTypeAdapter(Gender::class.java, GenderTypeAdapter())
            .registerTypeAdapter(CharacterStatus::class.java, CharacterStatusTypeAdapter())
            .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
            .pingInterval(DEFAULT_PING_INTERVAL, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
}