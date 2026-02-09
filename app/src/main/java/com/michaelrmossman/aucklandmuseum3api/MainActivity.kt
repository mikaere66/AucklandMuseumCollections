package com.michaelrmossman.aucklandmuseum3api

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication.Companion.instance
import com.michaelrmossman.aucklandmuseum3api.ui.MuseumApp
import com.michaelrmossman.aucklandmuseum3api.ui.theme.AucklandMuseumV3APITheme

class MainActivity: ComponentActivity() {

    private lateinit var bitmap: Bitmap

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        /* Handle the splash screen transition.
           Note how it comes BEFORE onCreate().
           On versions EARLIER than Android 12,
           all it does is prevent the blinding
           white that you get in Dark mode */
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AucklandMuseumV3APITheme {
                val layoutDirection = LocalLayoutDirection.current
                Surface(
                    modifier = Modifier
                        .padding(
                            start = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateStartPadding(layoutDirection),
                            end = WindowInsets.safeDrawing.asPaddingValues()
                                .calculateEndPadding(layoutDirection)
                        )
                ) {
                    val windowSize = calculateWindowSizeClass(
                        this@MainActivity
                    )
                    MuseumApp(
                        onSaveImageToFile = { bitmap, filename ->
                            this@MainActivity.bitmap = bitmap
                            createFileInDownloads(
                                filename = filename
                            )
                        },
                        windowWidthSize = windowSize.widthSizeClass,
                        windowHeightSize = windowSize.heightSizeClass
                    )
                }
            }
        }
    }

    /* Call initiated from ImageInfoBottomSheet() */
    private fun createFileInDownloads(filename: String) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/jpeg" // Must be "jpeg" to force file extension
            putExtra(Intent.EXTRA_TITLE,filename)
        }
        launcher.launch(intent)
    }

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val context = instance.applicationContext
        var length = Toast.LENGTH_LONG
        var message = context.getString(R.string.images_save_failed)

        if (result.resultCode == RESULT_OK) {

            result.data?.data?.let { uri ->

                val resolver = context.contentResolver
                try {
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        bitmap.compress(
                            Bitmap.CompressFormat.JPEG,
                            100, outputStream
                        )
                        outputStream.flush()
                    }
                    // File successfully written, show confirmation
                    message = context.getString(
                        R.string.images_save_success
                    )
                    length = Toast.LENGTH_SHORT

                } catch (e: Exception) {
                    e.printStackTrace()
                    // If an error occurs, delete the created entry
                    resolver.delete(uri,null,null)
                }
            }
        }

        Toast.makeText(context,message,length).show()
    }
}