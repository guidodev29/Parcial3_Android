package com.api.parcial3_video

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.graphics.drawable.toBitmap
import coil.compose.rememberAsyncImagePainter
import java.io.File

fun loadVideos(context: Context): List<Uri> {
    val directory = File(context.getExternalFilesDir(null), "MyAppVideos")
    return if (directory.exists() && directory.isDirectory) {
        directory.listFiles()?.map { it.toUri() } ?: emptyList()
    } else {
        emptyList()
    }
}

@Composable
fun VideoGalleryScreen(onBack: () -> Unit, onVideoClick: (Uri) -> Unit, context: Context) {
    val videoFiles = loadVideos(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Button(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Regresar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(videoFiles) { uri ->
                val painter = rememberAsyncImagePainter(model = uri) // Muestra la miniatura del video
                Image(
                    painter = painter,
                    contentDescription = "Miniatura del Video",
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .clickable { onVideoClick(uri) }
                )
            }
        }
    }
}
