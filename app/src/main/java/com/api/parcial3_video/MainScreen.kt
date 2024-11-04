package com.api.parcial3_video

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.ColorFilter

@Composable
fun MainScreen(
    onCapturePhoto: () -> Unit,
    onRecordVideo: () -> Unit,
    onViewGallery: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo de pantalla",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título de la app
            Text(
                text = "ElRollo",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Texto introductorio
            Text(
                text = "Bienvenido a ElRollo, tu compañero de aventuras turisticas, siempre estará contigo, ya que este rollo no tiene límite. Captura momentos inolvidables en la naturaleza y compártelos con el mundo.",
                fontSize = 16.sp,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.9f)
                    .padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Tomar Foto"
            Button(
                onClick = onCapturePhoto,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Tomar Foto", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Grabar Video"
            Button(
                onClick = onRecordVideo,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Grabar Video", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Ver Galería"
            Button(
                onClick = onViewGallery,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF57C00)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 32.dp)
            ) {
                Text(text = "Ver Galería", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}
