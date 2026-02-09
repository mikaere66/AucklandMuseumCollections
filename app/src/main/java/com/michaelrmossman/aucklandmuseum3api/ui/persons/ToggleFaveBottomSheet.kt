package com.michaelrmossman.aucklandmuseum3api.ui.persons

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.ui.components.ToggleFaveContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleFaveBottomSheet(
    itemId: String,
    onDismissRequest: () -> Unit,
    subtitle: String,
    title: String
) {
    val context = LocalContext.current
    var isFavourite by remember { mutableStateOf<Boolean?>(null) }
    var toggleFaveResult by remember { mutableStateOf<Int?>(null) }
    var toggleFavourite by remember { mutableStateOf(false) }
    val viewModel: PersonsViewModel = viewModel(
        factory = PersonsViewModel.Factory
    )

    LaunchedEffect(key1 = Unit) {
        isFavourite = viewModel.isFavourite(
            itemId = itemId
        )
    }

    LaunchedEffect(key1 = toggleFaveResult) {
        /* Refer note in VM regarding return values */
        toggleFaveResult?.let { result ->
            isFavourite?.let { favourite ->
                val stringId = when (result) {
                    0 -> R.string.faves_error
                    else -> when (favourite) {
                        true -> R.string.fave_removed
                        else -> R.string.fave_added
                    }
                }
                Toast.makeText(
                    context,
                    stringId,
                    when (result) {
                        0 -> Toast.LENGTH_LONG
                        else -> Toast.LENGTH_SHORT
                    }
                ).show()

                when (result) {
                    0 -> toggleFavourite = false
                    /* Dismiss the bottomSheet upon
                       successful add / rem fave */
                    else -> onDismissRequest()
                }
            }
        }
    }

    LaunchedEffect(key1 = toggleFavourite) {
        if (toggleFavourite) {
            toggleFaveResult = viewModel.toggleFavourite(
                isFavourite = isFavourite == true,
                itemId = itemId,
                itemType = MediaType.Person,
                subtitle = subtitle,
                title = title
            )
        }
    }

    isFavourite?.let { favourite ->
        android.util.Log.d("'$itemId'",favourite.toString())
        ToggleFaveContent(
            isFavourite = favourite,
            mediaType = MediaType.Person,
            onDismissRequest = { onDismissRequest() },
            onToggleFave = { toggleFavourite = true },
            title = title
        )
    }
}