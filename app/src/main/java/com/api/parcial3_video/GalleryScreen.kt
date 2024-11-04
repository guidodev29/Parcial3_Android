package com.api.parcial3_video

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import android.media.ThumbnailUtils
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.api.parcial3_video.ui.theme.BrownPrimary
import com.api.parcial3_video.ui.theme.TextWhite
import java.io.File

fun loadMedia(context: Context): List<Pair<Uri, Boolean>> {
    val photoDirectory = File(context.getExternalFilesDir(null), "MyAppPhotos")
    val videoDirectory = File(context.getExternalFilesDir(null), "MyAppVideos")

    val photoUris = photoDirectory.listFiles()?.map { it.toUri() to false } ?: emptyList() // `false` indica que es una foto
    val videoUris = videoDirectory.listFiles()?.map { it.toUri() to true } ?: emptyList() // `true` indica que es un video

    return photoUris + videoUris
}

@Composable
fun GalleryScreen(onBack: () -> Unit, onMediaClick: (Uri, Boolean) -> Unit, context: Context) {
    val mediaFiles = loadMedia(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F5E1)) // Fondo Beige Claro
            .padding(16.dp)
    ) {
        Column {
            // Botón de regreso estilizado
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(containerColor = BrownPrimary),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .shadow(4.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
            ) {
                Text("Regresar", color = TextWhite, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cuadrícula de la galería
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(mediaFiles) { (uri, isVideo) ->
                    val painter = if (isVideo) {
                        rememberVideoThumbnailPainter(uri, context)
                    } else {
                        rememberAsyncImagePainter(model = uri)
                    }
                    Image(
                        painter = painter,
                        contentDescription = if (isVideo) "Miniatura del Video" else "Miniatura de la Foto",
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxWidth()
                            .clickable { onMediaClick(uri, isVideo) }
                            .shadow(4.dp, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun rememberVideoThumbnailPainter(uri: Uri, context: Context): Painter {
    val thumbnail: ImageBitmap? = remember(uri) {
        ThumbnailUtils.createVideoThumbnail(
            uri.path!!,
            MediaStore.Images.Thumbnails.MINI_KIND
        )?.asImageBitmap()
    }

    return if (thumbnail != null) {
        BitmapPainter(thumbnail)
    } else {
        painterResource(id = R.drawable.ic_video_placeholder) // Asegúrate de tener este recurso en drawable
    }
}
