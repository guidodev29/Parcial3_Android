package com.api.parcial3_video

import PhotoDetailScreen
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    private val capturePhotoLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            savePhotoToAppFolder(it)
        } ?: run {
            Toast.makeText(this, "Error al capturar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    private val captureVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val videoUri: Uri? = result.data?.data
        if (videoUri != null) {
            saveVideoToAppFolder(videoUri)
        } else {
            Toast.makeText(this, "Error al capturar el video", Toast.LENGTH_SHORT).show()
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startVideoRecording()
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            var showGallery by remember { mutableStateOf(false) }
            var selectedMedia by remember { mutableStateOf<Pair<Uri, Boolean>?>(null) }

            when {
                showGallery -> {
                    GalleryScreen(
                        onBack = { showGallery = false },
                        onMediaClick = { uri, isVideo ->
                            selectedMedia = uri to isVideo
                            showGallery = false
                        },
                        context = this
                    )
                }
                selectedMedia != null -> {
                    val (uri, isVideo) = selectedMedia!!
                    if (isVideo) {
                        VideoPlayerScreen(
                            videoUri = uri,
                            onBack = { selectedMedia = null }
                        )
                    } else {
                        PhotoDetailScreen(
                            photoFile = File(uri.path),
                            onBack = { selectedMedia = null },
                            context = this
                        )
                    }
                }
                else -> {
                    MainScreen(
                        onCapturePhoto = { checkCameraPermissionAndCapture() },
                        onViewGallery = { showGallery = true },
                        onRecordVideo = { startVideoRecording() }
                    )
                }
            }
        }
    }

    private fun checkCameraPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            capturePhotoLauncher.launch(null)
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startVideoRecording() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            captureVideoLauncher.launch(intent)
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun savePhotoToAppFolder(bitmap: Bitmap) {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        val directory = File(getExternalFilesDir(null), "MyAppPhotos")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, filename)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
        }
        Toast.makeText(this, "Foto guardada en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
    }

    private fun saveVideoToAppFolder(videoUri: Uri) {
        val directory = File(getExternalFilesDir(null), "MyAppVideos")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val videoFile = File(directory, "VID_${System.currentTimeMillis()}.mp4")
        contentResolver.openInputStream(videoUri)?.use { input ->
            videoFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Usar MediaScannerConnection para escanear el nuevo archivo de video
        MediaScannerConnection.scanFile(this, arrayOf(videoFile.absolutePath), null) { path, uri ->
            runOnUiThread {
                Toast.makeText(this, "Video guardado en: $path", Toast.LENGTH_LONG).show()
            }
        }
    }
}
