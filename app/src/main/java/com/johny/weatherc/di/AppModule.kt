package com.johny.weatherc.di

import android.content.Context
import android.net.http.NetworkException
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.HTTP
import java.io.File
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String = "https://api.open-meteo.com"

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        @Named("baseUrl") baseUrl: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("responseInterceptor") responseInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
//        .addInterceptor(responseInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    @Named("responseInterceptor")
    fun provideErrorInterceptor(): Interceptor =
        Interceptor { chain ->
            try {
                val response = chain.proceed(chain.request())
                if (!response.isSuccessful) {
                    generateFailedResponse(chain.request(), response.code, response.message)
                } else {
                    response
                }
            } catch (e: SocketTimeoutException) {
                generateFailedResponse(chain.request(), 0, "Request timeout")
            } catch (e: UnknownHostException) {
                generateFailedResponse(chain.request(), 0, "No internet connection")
            } catch (e: IOException) {
                generateFailedResponse(chain.request(), 0, "Network error: ${e.message}")
            } catch (e: Exception) {
                generateFailedResponse(chain.request(), 0, "Error: ${e.message}")
            }
        }

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val file = File(context.externalCacheDir, "cache_api")
        return Cache(file, (50 * 1024 * 1024).toLong())
    }

    private fun generateFailedResponse(request: Request, code: Int = 0, message: String = "Unknown error"): Response {
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("")
            .body(
                Gson().toJson(mapOf(
                    "code" to code,
                    "message" to message,
                    "data" to Gson()
                )).toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }

}