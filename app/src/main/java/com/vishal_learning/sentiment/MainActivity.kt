package com.vishal_learning.sentiment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.vishal_learning.sentiment.Presentation.MainActivityViewModel
import com.vishal_learning.sentiment.ui.theme.SentimentTheme
import com.vishal_learning.sentiment.ui.theme.btnColor
import com.vishal_learning.sentiment.ui.theme.heading
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.vishal_learning.sentiment.Model.Api
import retrofit2.Response


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SentimentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
                    Sentiment(viewModel)
                }
            }
        }
    }
}

@Composable
fun Sentiment(viewModel: MainActivityViewModel) {
    var textState by remember { mutableStateOf("") }
    val result by viewModel.result.observeAsState("")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sentiment Analyzer",
            fontSize = 28.sp,
            color = Color.Black,
            fontFamily = FontFamily.Serif
        )

        OutlinedTextField(
            modifier = Modifier.size(340.dp),
            value = textState,
            onValueChange = { textState = it },
            placeholder = { Text("Enter your Text") }
        )

        Button(
            onClick = {
                // Call ViewModel method to get sentiment
                viewModel.getSentiments(textState)
            },
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(btnColor),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Text(
                "Classify Text",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        if (result.isNotEmpty()) {
            Text(
                text = result,
                fontSize = 18.sp
            )
        }
    }
}
