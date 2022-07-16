package com.example.holidaysoverlapp.di

import com.example.holidaysoverlapp.data.Constants.API_KEY
import com.example.holidaysoverlapp.data.Constants.BASE_URL
import com.example.holidaysoverlapp.data.api.HolidaysService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): HolidaysService =
        retrofit.create(HolidaysService::class.java)

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Singleton
    @Provides
    fun provideInterceptor(): Interceptor = Interceptor { chain ->
        val original = chain.request()
        val url = original.url()
        val newUrl = url.newBuilder().addQueryParameter("key", API_KEY).build()
        val requestBuilder = original.newBuilder().url(newUrl)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

}