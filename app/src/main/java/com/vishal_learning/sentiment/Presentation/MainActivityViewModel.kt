package com.vishal_learning.sentiment.Presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishal_learning.sentiment.Model.ApiNetwork
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    fun getSentiments(textState: String) {
        viewModelScope.launch {
            try {
                val response = ApiNetwork.retrofit.getSentiment(textState)
                response?.let {
                    if (it.isNotEmpty()) {
                        val highestScoreLabel =
                            it[0].maxByOrNull { apiResponse -> apiResponse.body()?.score ?: 0.0 }
                                ?.body()?.label
                        val highestScore =
                            it[0].maxByOrNull { apiResponse -> apiResponse.body()?.score ?: 0.0 }
                                ?.body()?.score
                        _result.value = "$highestScoreLabel\n$highestScore"
                    }
                }
            } catch (e: Exception) {
                // Handle errors appropriately (e.g., show error message to user)
                _result.value = "Error: ${e.message}"
            }
        }
    }
}


