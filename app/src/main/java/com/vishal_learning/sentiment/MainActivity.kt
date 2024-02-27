package com.vishal_learning.sentiment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.vishal_learning.sentiment.Presentation.SentimentViewModel
import com.vishal_learning.sentiment.ui.theme.SentimentTheme
import com.vishal_learning.sentiment.ui.theme.btnColor
import com.vishal_learning.sentiment.ui.theme.heading
import java.util.Locale


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: SentimentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
            .get(SentimentViewModel::class.java)
        setContent {
            SentimentTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Sentiment(viewModel)
                }
            }
        }
    }
}

@Composable
fun Sentiment(viewModel: SentimentViewModel) {
    var textState by remember { mutableStateOf("") }
    val result by viewModel.result.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sentiment Analyzer",
            fontSize = 32.sp,
            color = heading,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(top=6.dp)
        )

        OutlinedTextField(
            modifier = Modifier.size(340.dp),
            value = textState,
            onValueChange = { textState = it },
            placeholder = { Text("Enter your Text here") }
        )

        Button(
            onClick = {
                viewModel.performSentimentAnalysis(textState)
            },
            shape = RoundedCornerShape(7.dp),
            colors = ButtonDefaults.buttonColors(btnColor),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            Text("Classify Text", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        result?.let {
            val textColor = when (it) {
                "positive" -> Color.Green
                "negative" -> Color.Red
                else -> Color.Black
            }
            Text(
                text = it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                color = textColor
            )
        }
        AnimatedResult(result = result)
    }
}
@Composable
fun AnimatedResult(result: String?) {
    // Define animX and animY as mutable state variables
    val animX by remember { mutableStateOf(Animatable(0f)) }
    val animY by remember { mutableStateOf(Animatable(0f)) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val compositionResult = (if (result == "positive") R.raw.happy else if (result=="negative") R.raw.angry else null)?.let {
            LottieCompositionSpec.RawRes(
                it
            )
        }?.let { rememberLottieComposition(spec = it) }
        val preloaderProgress by animateLottieCompositionAsState(
            compositionResult?.value,
            iterations = LottieConstants.IterateForever,
            isPlaying = true
        )
        compositionResult?.value?.let { composition ->
            val offsetX = with(LocalDensity.current) { animX.value.toDp() }
            val offsetY = with(LocalDensity.current) { animY.value.toDp() }
            LottieAnimation(
                composition = composition,
                progress = preloaderProgress,
                modifier = Modifier.offset(x = offsetX, y = offsetY),

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview(){

}