package com.example.indicedmasamuscular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavegacion()
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "pantallaInicio") {
        composable("pantallaInicio") {
            PantallaInicio(navController)
        }
        composable("resultado/{imc}/{clasificacion}") { backStackEntry ->
            val imc = backStackEntry.arguments?.getString("imc") ?: "0.0"
            val clasificacion = backStackEntry.arguments?.getString("clasificacion") ?: "Desconocido"
            PantallaResultado(imc, clasificacion)
        }
    }
}

@Composable
fun PantallaInicio(navController: NavController) {
    var peso by remember { mutableStateOf("") }
    var estatura by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Calculadora de IMC", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = peso,
            onValueChange = { peso = it },
            label = { Text("Peso (kg)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = estatura,
            onValueChange = { estatura = it },
            label = { Text("Estatura (m)") }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val pesoNum = peso.toFloatOrNull()
            val estaturaNum = estatura.toFloatOrNull()
            if (pesoNum != null && estaturaNum != null && estaturaNum > 0) {
                val imc = pesoNum / (estaturaNum * estaturaNum)
                val clasificacion = clasificarIMC(imc)
                navController.navigate("resultado/${"%.2f".format(imc)}/$clasificacion")
            }
        }) {
            Text("Calcular IMC")
        }
    }
}

fun clasificarIMC(imc: Float): String = when {
    imc <= 18.4 -> "Bajo peso"
    imc <= 24.9 -> "Normal"
    imc <= 29.9 -> "Sobrepeso"
    imc <= 34.9 -> "Obesidad clase 1"
    imc <= 39.9 -> "Obesidad clase 2"
    else -> "Obesidad clase 3"
}

@Composable
fun PantallaResultado(imc: String, clasificacion: String) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Resultado del IMC", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tu IMC es: $imc", fontSize = 20.sp)
        Text("Clasificación: $clasificacion", fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    AppNavegacion()
}
