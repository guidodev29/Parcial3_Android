import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color as AndroidColor // Renombrar para los filtros
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.api.parcial3_video.ui.theme.BrownPrimary
import com.api.parcial3_video.ui.theme.GreenPrimary
import com.api.parcial3_video.ui.theme.TextWhite
import java.io.File
import java.io.FileOutputStream

@Composable
fun PhotoDetailScreen(photoFile: File, onBack: () -> Unit, context: Context) {
    var bitmap by remember { mutableStateOf<Bitmap?>(BitmapFactory.decodeFile(photoFile.absolutePath)) }
    var filteredBitmap by remember { mutableStateOf(bitmap) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8F5E1)), // Fondo Beige Claro en Compose
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        filteredBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Foto en Detalle",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.White) // Blanco en Compose
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de Filtro en múltiples filas
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { filteredBitmap = bitmap },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Sin Filtro", color = TextWhite, fontSize = 12.sp)
                }
                Button(
                    onClick = { filteredBitmap = bitmap?.let { applyGrayScaleFilter(it) } },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Blanco y Negro", color = TextWhite, fontSize = 12.sp)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { filteredBitmap = bitmap?.let { applySepiaFilter(it) } },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Sepia", color = TextWhite, fontSize = 12.sp)
                }
                Button(
                    onClick = { filteredBitmap = bitmap?.let { applyInvertFilter(it) } },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Invertir Colores", color = TextWhite, fontSize = 12.sp)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { filteredBitmap = bitmap?.let { applyBrightnessFilter(it, 50) } },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Más Brillo", color = TextWhite, fontSize = 12.sp)
                }
                Button(
                    onClick = { filteredBitmap = bitmap?.let { applySaturationFilter(it, 1.5f) } },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Saturación", color = TextWhite, fontSize = 12.sp)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { filteredBitmap = bitmap?.let { applyColorEraseFilter(it) } },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Borrador de Color", color = TextWhite, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para Guardar Foto
        Button(
            onClick = {
                filteredBitmap?.let { bmp ->
                    savePhotoWithFilter(bmp, photoFile, context)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Guardar Foto", color = TextWhite, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de Regresar
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
        ) {
            Text("Regresar", color = TextWhite, fontSize = 16.sp)
        }
    }

}


// Funciones de Filtro
fun applyGrayScaleFilter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val grayBitmap = Bitmap.createBitmap(width, height, bitmap.config)
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            val r = AndroidColor.red(pixel)
            val g = AndroidColor.green(pixel)
            val b = AndroidColor.blue(pixel)
            val gray = (r + g + b) / 3
            grayBitmap.setPixel(x, y, AndroidColor.rgb(gray, gray, gray))
        }
    }
    return grayBitmap
}

fun applySepiaFilter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val sepiaBitmap = Bitmap.createBitmap(width, height, bitmap.config)
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            val r = AndroidColor.red(pixel)
            val g = AndroidColor.green(pixel)
            val b = AndroidColor.blue(pixel)
            val tr = (0.393 * r + 0.769 * g + 0.189 * b).toInt().coerceAtMost(255)
            val tg = (0.349 * r + 0.686 * g + 0.168 * b).toInt().coerceAtMost(255)
            val tb = (0.272 * r + 0.534 * g + 0.131 * b).toInt().coerceAtMost(255)
            sepiaBitmap.setPixel(x, y, AndroidColor.rgb(tr, tg, tb))
        }
    }
    return sepiaBitmap
}

fun applyInvertFilter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val invertedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            val r = 255 - AndroidColor.red(pixel)
            val g = 255 - AndroidColor.green(pixel)
            val b = 255 - AndroidColor.blue(pixel)
            invertedBitmap.setPixel(x, y, AndroidColor.rgb(r, g, b))
        }
    }
    return invertedBitmap
}

fun applyBrightnessFilter(bitmap: Bitmap, value: Int): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val brightBitmap = Bitmap.createBitmap(width, height, bitmap.config)
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            val r = (AndroidColor.red(pixel) + value).coerceIn(0, 255)
            val g = (AndroidColor.green(pixel) + value).coerceIn(0, 255)
            val b = (AndroidColor.blue(pixel) + value).coerceIn(0, 255)
            brightBitmap.setPixel(x, y, AndroidColor.rgb(r, g, b))
        }
    }
    return brightBitmap
}

fun applySaturationFilter(bitmap: Bitmap, saturation: Float): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val saturatedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            val r = (AndroidColor.red(pixel) * saturation).toInt().coerceIn(0, 255)
            val g = (AndroidColor.green(pixel) * saturation).toInt().coerceIn(0, 255)
            val b = (AndroidColor.blue(pixel) * saturation).toInt().coerceIn(0, 255)
            saturatedBitmap.setPixel(x, y, AndroidColor.rgb(r, g, b))
        }
    }
    return saturatedBitmap
}

fun applyColorEraseFilter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val erasedBitmap = Bitmap.createBitmap(width, height, bitmap.config)
    for (x in 0 until width) {
        for (y in 0 until height) {
            val pixel = bitmap.getPixel(x, y)
            val r = AndroidColor.red(pixel)
            val g = AndroidColor.green(pixel)
            val b = AndroidColor.blue(pixel)
            val gray = (r + g + b) / 3
            if (gray > 100) {
                erasedBitmap.setPixel(x, y, AndroidColor.rgb(r, g, b))
            } else {
                erasedBitmap.setPixel(x, y, AndroidColor.rgb(gray, gray, gray))
            }
        }
    }
    return erasedBitmap
}
fun savePhotoWithFilter(bitmap: Bitmap, file: File, context: Context) {
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
    }
    Toast.makeText(context, "Foto guardada en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
}
