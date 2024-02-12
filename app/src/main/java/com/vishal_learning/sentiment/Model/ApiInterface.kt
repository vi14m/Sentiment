package com.vishal_learning.sentiment.Model

<<<<<<< HEAD
import retrofit2.Response
=======
>>>>>>> 1d2de2d (Complete)
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SentimentApiService {
    @Headers("Authorization: Bearer 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")
    @POST("models/cardiffnlp/twitter-roberta-base-sentiment-latest")
<<<<<<< HEAD
    suspend fun getSentiment(@Body payload:String): List<List<Response<Api>>>?
}

=======
    suspend fun analyzeSentiment(@Body payload: Map<String, String>): List<List<SentimentResponse>>
}


>>>>>>> 1d2de2d (Complete)
