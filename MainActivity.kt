package com.example.jornadadaconquista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.ViewModel
import com.example.jornadadaconquista.ui.theme.JornadaDaConquistaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: JourneyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JornadaDaConquistaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    JourneyScreen(viewModel)
                }
            }
        }
    }
}

class JourneyViewModel : ViewModel() {
    var clickCount by mutableIntStateOf(0)
    var currentImage by mutableIntStateOf(R.drawable.pote_vazio_sem_fundo)
    var journeyCompleted by mutableStateOf(false)
    var showDialog by mutableStateOf(false)
    private val Num = (1..50).random()

    fun onImageClick() {
        if (!journeyCompleted) {
            clickCount++
            updateImage()
            if (clickCount == Num) {
                journeyCompleted = true
                showDialog = true
            }
        }
    }

    fun onGiveUpClick() {
        currentImage = R.drawable.give_up_image
        showDialog = true
    }

    fun restartGame() {
        clickCount = 0
        currentImage = R.drawable.pote_vazio_sem_fundo
        journeyCompleted = false
        showDialog = false
    }

    private fun updateImage() {
        val progress = clickCount.toFloat() / Num
        currentImage = when {
            progress <= 0.33 -> R.drawable.pote_vazio_sem_fundo
            progress <= 0.66 -> R.drawable.pote_medio_sem_fundo
            progress < 1.0 -> R.drawable.pote_cheio_sem_fundo
            else -> R.drawable.tio_patinhas
        }
    }
}

@Composable
fun JourneyScreen(viewModel: JourneyViewModel) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contador no topo
            Text(
                text = "Cliques: ${viewModel.clickCount}",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLandscape) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = viewModel.currentImage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .clickable { viewModel.onImageClick() }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { viewModel.onGiveUpClick() }) {
                            Text(text = "Desistir")
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = viewModel.currentImage),
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .clickable { viewModel.onImageClick() }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.onGiveUpClick() }) {
                        Text(text = "Desistir")
                    }
                }
            }
        }

        if (viewModel.showDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showDialog = false },
                title = { Text("Novo Jogo?") },
                text = {
                    Text(
                        if (viewModel.journeyCompleted) "Parabéns! Você completou a jornada!"
                        else "Você desistiu! Deseja começar novamente?"
                    )
                },
                confirmButton = {
                    Button(onClick = { viewModel.restartGame() }) {
                        Text("Sim")
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.showDialog = false }) {
                        Text("Não")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val viewModel = JourneyViewModel()
    JornadaDaConquistaTheme {
        JourneyScreen(viewModel)
    }
}

//Claudiano Alves, Davi Fabrício, Jean Prado
//RGM: 33958238, 34313001, 8812295272
