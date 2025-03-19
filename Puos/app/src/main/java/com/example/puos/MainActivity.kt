package com.example.puos

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var gestorSensores: SensorManager
    private var ultimaActualizacion: Long = 0
    private val umbralMovimiento = 10f

    private val estadoGolpeando = mutableStateOf(false)

    private var reproductorAudio: MediaPlayer? = null

    private val sonidoNormal = R.raw.sonido1_1
    private val sonidoEspecial = R.raw.sonido3

    private var contadorGolpes = 0

    private val manejador = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gestorSensores = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        setContent {
            AplicacionGolpe(isPunching = estadoGolpeando.value)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val tiempoActual = System.currentTimeMillis()
            if ((tiempoActual - ultimaActualizacion) > 100) {
                ultimaActualizacion = tiempoActual

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val aceleracion = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

                if (aceleracion > umbralMovimiento && !estadoGolpeando.value) {
                    contadorGolpes++

                    if (contadorGolpes >= 3) {
                        activarMovimiento(sonidoEspecial)
                        contadorGolpes = 0
                    } else {
                        activarMovimiento(sonidoNormal)
                    }
                }
            }
        }
    }

    private fun activarMovimiento(sonido: Int) {
        estadoGolpeando.value = true

        if (reproductorAudio == null) {
            reproductorAudio = MediaPlayer.create(this, sonido)
        }
        reproductorAudio?.start()

        manejador.postDelayed({
            estadoGolpeando.value = false
        }, 300)
        reproductorAudio = null
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        gestorSensores.registerListener(
            this,
            gestorSensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        gestorSensores.unregisterListener(this)
        reproductorAudio?.release()
        reproductorAudio = null
    }
}

@Composable
fun AplicacionGolpe(isPunching: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(
                id = if (isPunching) R.drawable.punos2 else R.drawable.punos1
            ),
            contentDescription = if (isPunching) "Fist punching" else "Fist idle",
            modifier = Modifier.size(300.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VistaGolpe() {
    AplicacionGolpe(isPunching = false)
}
