package com.api.parcial3_video

import android.net.Uri
import android.widget.MediaController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.api.parcial3_video.ui.theme.BrownPrimary
import com.api.parcial3_video.ui.theme.TextWhite
import android.widget.VideoView

@Composable
fun VideoPlayerScreen(videoUri: Uri, onBack: () -> Unit) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5E1)) // Fondo Beige Claro
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Reproductor de video en pantalla completa
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoURI(videoUri)
                        setMediaController(MediaController(ctx).apply {
                            setAnchorView(this@apply)
                        })
                        start()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f) // Usa el 80% de la altura para que sea más grande
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
}

