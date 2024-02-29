package com.vishal_learning.sentiment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
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
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sentiment(viewModel: SentimentViewModel) {
    var textState by remember { mutableStateOf("") }
    val result by viewModel.result.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Draw the custom shape behind the main UI
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomShape()
        }

        // Main UI components with elevation
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .offset(y = (-20).dp) // Offset to visually elevate the UI
                .background(Color.Transparent, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)), // Rounded corners only at the top
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sentiment Analyzer",
                fontSize = 32.sp,
                color = heading,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(top = 6.dp)
            )

            OutlinedTextField(
                modifier = Modifier.size(340.dp),
                value = textState,
                onValueChange = { textState = it },
                placeholder = { Text("Enter your Text here") },
                colors = OutlinedTextFieldDefaults.colors(
                    Color.Black
                )
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
}



@Composable
fun AnimatedResult(result: String?) {
    val animatableOffsetY = remember { Animatable(15f) }

    LaunchedEffect(Unit) {
        animatableOffsetY.animateTo(-32f, animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ))
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val compositionResult =
            (if (result == "positive") R.raw.happy else if (result == "negative") R.raw.angry else null)?.let {
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

            Box(
                modifier = Modifier
                    .size(399.dp) // Adjust the size as needed
                    .offset(y = animatableOffsetY.value.dp) // Apply the animated offset

            ) {
                LottieAnimation(
                    composition = composition,
                    progress = preloaderProgress,
                    modifier = Modifier.offset(x = 0.dp, y = 0.dp),
                )
            }

        }
    }
}



@Composable
fun CustomShape() {
    val animY by remember { mutableFloatStateOf(-400f) }
    val offsetY = with(LocalDensity.current) { animY.toDp() }

    Box(
        modifier = Modifier
            .size(399.dp) // Adjust the size as needed
            .offset(y = offsetY) // Apply the animated offset

    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCustomShape(
                topLeftX = 0f,
                topLeftY = 0f,
                width = size.width,
                height = size.height,
                drawScope = this
            )
        }
    }
}

private fun DrawScope.drawCustomShape(
    topLeftX: Float,
    topLeftY: Float,
    width: Float,
    height: Float,
    drawScope: DrawScope
) {
    val path = Path().apply {
        val radius1 = width * 0.67f
        val radius2 = width * 0.33f
        val radius3 = height * 0.61f
        val radius4 = height * 0.39f

        moveTo(topLeftX + radius1, topLeftY)
        lineTo(topLeftX + width - radius2, topLeftY)
        quadraticBezierTo(topLeftX + width, topLeftY, topLeftX + width, topLeftY + radius3)
        lineTo(topLeftX + width, topLeftY + height - radius4)
        quadraticBezierTo(
            topLeftX + width,
            topLeftY + height,
            topLeftX + width - radius2,
            topLeftY + height
        )
        lineTo(topLeftX + radius1, topLeftY + height)
        quadraticBezierTo(topLeftX, topLeftY + height, topLeftX, topLeftY + height - radius4)
        lineTo(topLeftX, topLeftY + radius3)
        quadraticBezierTo(topLeftX, topLeftY, topLeftX + radius1, topLeftY)
        close()
    }

    drawScope.drawPath(path, color = Color.Blue.copy(0.23f))
}

@Preview(showBackground = true)
@Composable
fun SentimentPreview() {
    val viewModel = SentimentViewModel() // Create a dummy view model for preview
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Sentiment(viewModel = viewModel)
    }
}


