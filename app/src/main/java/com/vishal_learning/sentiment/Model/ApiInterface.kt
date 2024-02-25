package com.vishal_learning.sentiment.Model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SentimentApiService {
    @Headers("Authorization: Bearer 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")
    @POST("models/cardiffnlp/twitter-roberta-base-sentiment-latest")
    suspend fun analyzeSentiment(@Body payload: Map<String, String>): List<List<SentimentResponse>>
}

