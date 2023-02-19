package com.wahyush04.androidphincon.core.di

import android.app.Activity
import android.content.Context
import com.wahyush04.androidphincon.api.ApiService
import com.wahyush04.androidphincon.api.AuthAuthenticator
import com.wahyush04.androidphincon.api.AuthBadResponse
import com.wahyush04.androidphincon.api.HeaderInterceptor
import com.wahyush04.androidphincon.core.repository.IRepository
import com.wahyush04.androidphincon.core.repository.Repository
import com.wahyush04.androidphincon.paging.ProductPagingSource
import com.wahyush04.androidphincon.ui.loading.LoadingDialog
import com.wahyush04.core.Constant.Companion.BASE_URL
import com.wahyush04.core.helper.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authBadResponse: AuthBadResponse,
        authAuthenticator: AuthAuthenticator,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor) //header
            .addInterceptor(authBadResponse) // 401 bad response
            .authenticator(authAuthenticator) // get refresh token
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providePreferenceHelper(@ApplicationContext context: Context): PreferenceHelper =
        PreferenceHelper(context)

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        preference: PreferenceHelper,
        @ApplicationContext context: Context
    ): AuthBadResponse = AuthBadResponse(preference, context)

    @Singleton
    @Provides
    fun provideAuthAuthenticator(preferences: PreferenceHelper): Authenticator =
        AuthAuthenticator(preferences)

    @Singleton
    @Provides
    fun provideHeaderInterceptor(preference: PreferenceHelper): HeaderInterceptor =
        HeaderInterceptor(preference)

    @Singleton
    @Provides
    fun provideRepository(apiserice: ApiService): IRepository =
        Repository(apiserice)

}