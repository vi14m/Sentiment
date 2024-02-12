package com.vishal_learning.sentiment.Model

import retrofit2.Retrofit

object ApiNetwork {
    val retrofit: SentimentApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api-inference.huggingface.co/")
            .build()
            .create(SentimentApiService::class.java)
    }
}