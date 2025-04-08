package com.example.proyect2

import android.os.Bundle
import android.util.Base64
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIPrincipal()
        }
    }
}

@Composable
fun UIPrincipal() {
    val dbManager = DBHelper(LocalContext.current) // Instanciamos la base
    val productos = remember { mutableStateOf(dbManager.obtenerProductos()) }
    // Barra superior personalizada con el botón para añadir producto


    // Barra superior personalizada con el botón para añadir producto
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Aplicamos el padding general al contenido
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Barra superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Espaciado inferior para separar el contenido de la barra superior
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Productos Disponibles", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                IconButton(onClick = { /* Acción para añadir producto */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Añadir Producto")
                }
            }

            // Contenido de la lista de productos
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(productos.value) { producto ->
                    ProductoItem(producto)
                }
            }
        }
    }
}
// Función para convertir una cadena Base64 a Bitmap
fun convertirBase64AImagen(base64: String): Bitmap? {
    return try {
        val decodedString = Base64.decode(base64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Column(modifier = Modifier.padding(16.dp).border(1.dp, Color.LightGray)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            // Imagen del producto
            producto.imagenBase64?.let { base64 ->
                val bitmap = convertirBase64AImagen(base64)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Imagen del producto",
                        modifier = Modifier.size(128.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            } ?: run {
                Text("Imagen no disponible", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            // Detalles del producto (Texto) al lado de la imagen
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = producto.nombre, style = MaterialTheme.typography.bodyLarge)
                Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                producto.descripcion?.let {
                    Text(text = it, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }

        // Botones debajo de la imagen y el texto
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = { /* Acción pendiente */ }, modifier = Modifier.padding(8.dp)) {
                Text("Editar")
            }
            Button(onClick = { /* Acción pendiente */ }, modifier = Modifier.padding(8.dp)) {
                Text("Eliminar")
            }
        }
    }
}

data class Producto(
    val nombre: String,
    val precio: Double,
    val descripcion: String?,
    val imagenBase64: String?  // Cambié el nombre a imagenBase64 para ser más claro
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UIPrincipal()  // Muestra la interfaz definida para previsualizar
}
