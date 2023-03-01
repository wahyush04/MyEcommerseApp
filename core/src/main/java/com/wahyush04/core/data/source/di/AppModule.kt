package com.wahyush04.core.data.source.di

import android.content.Context
import androidx.room.Room
import com.wahyush04.core.Constant.Companion.BASE_URL
import com.wahyush04.core.api.ApiService
import com.wahyush04.core.api.AuthAuthenticator
import com.wahyush04.core.api.AuthBadResponse
import com.wahyush04.core.api.HeaderInterceptor
import com.wahyush04.core.data.source.local.LocalDataSource
import com.wahyush04.core.data.source.local.room.NotificationDao
import com.wahyush04.core.data.source.local.room.ProductDao
import com.wahyush04.core.data.source.local.room.ProductDatabase
import com.wahyush04.core.data.source.remote.RemoteDataSource
import com.wahyush04.core.data.source.repository.IRepository
import com.wahyush04.core.data.source.repository.Repository
import com.wahyush04.core.helper.PreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
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
    fun provideDatabase(@ApplicationContext context: Context): ProductDatabase =
        Room.databaseBuilder(
        context,
        ProductDatabase::class.java, "moystuff.db"
    ).fallbackToDestructiveMigration()
        .allowMainThreadQueries()
            .build()

    @Provides
    fun provideProductDao(database: ProductDatabase): ProductDao = database.productDao()

    @Provides
    fun provideNotificationDao(database: ProductDatabase): NotificationDao = database.notificationDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(cartDao: ProductDao, notificationDao: NotificationDao): LocalDataSource =
        LocalDataSource(cartDao, notificationDao)

    @Singleton
    @Provides
    fun provideRemoteDataSource(apiservice: ApiService): RemoteDataSource =
        RemoteDataSource(apiservice)

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): IRepository =
        Repository(remoteDataSource, localDataSource)

}