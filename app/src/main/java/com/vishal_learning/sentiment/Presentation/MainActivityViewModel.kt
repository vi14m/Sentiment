package com.vishal_learning.sentiment.Presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishal_learning.sentiment.Model.SentimentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SentimentViewModel : ViewModel() {
    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    fun performSentimentAnalysis(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _result.value = SentimentRepository().analyzeSentiment(text)[0][0].label
            } catch (e: Exception) {
                _result.value = "Error: ${e.message}"
            }
        }
    }
}



