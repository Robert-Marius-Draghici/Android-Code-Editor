package com.example.codeeditor.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://www.deepcode.ai/publicapi/"

interface CodeReviewService {
    @POST("login")
    suspend fun loginToDeepCode(): LoginDeepCodeResponse

    @GET("session")
    suspend fun checkDeepCodeSession(@Header("Session-Token") sessionToken: String)

    @POST("bundle")
    suspend fun createBundle(
        @Header("Session-Token") sessionToken: String,
        @Body data: CreateBundleRequest?
    ): CreateBundleResponse

    @GET("analysis/{bundleId}")
    suspend fun getAnalysis(
        @Header("Session-Token") sessionToken: String,
        @Path(value="bundleId", encoded=true) bundleId: String
    ): GetAnalysisResponse
}

object CodeReviewApi {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)
    }.build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    val retrofitService: CodeReviewService by lazy {
        retrofit.create(CodeReviewService::class.java)
    }
}

