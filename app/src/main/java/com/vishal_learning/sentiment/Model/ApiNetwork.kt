package com.vishal_learning.sentiment.Model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


class SentimentRepository {
    private val apiService: SentimentApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-inference.huggingface.co/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        apiService = retrofit.create(SentimentApiService::class.java)
    }

    suspend fun analyzeSentiment(text: String): List<List<SentimentResponse>> {
        val payload = mapOf("inputs" to text)
        return apiService.analyzeSentiment(payload)
    }
}
