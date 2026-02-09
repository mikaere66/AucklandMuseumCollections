package com.michaelrmossman.aucklandmuseum3api.ui.components

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication.Companion.instance
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObject
import com.michaelrmossman.aucklandmuseum3api.util.imageSizeFormatted
import com.michaelrmossman.aucklandmuseum3api.util.toByteArrayAndSizeInBytes
import java.io.File
import kotlinx.coroutines.launch

/* Info/save button for single image */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageInfoBottomSheet(
    imageObject: ImageObject,
    onDismissRequest: () -> Unit,
    onSaveImageToFile: (Bitmap, String) -> Unit,
    paddingHorizontal: Dp,
    paddingVertical: Dp,
    @StringRes stringId: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    val context = instance.applicationContext
    val coroutineScope = rememberCoroutineScope()
    val headerText = stringResource(R.string.image_info_desc).plus(":")
    val iconLargePadding = dimensionResource(R.dimen.padding_great)
    val iconSize = dimensionResource(R.dimen.icon_size_small)
    var imageSizeInBytes by remember { mutableDoubleStateOf(0.0) }

    val lazyListState = rememberLazyListState()
    val onImageBitmapLoaded = { bitmap: Bitmap ->
        imageSizeInBytes = bitmap.toByteArrayAndSizeInBytes()
    }
    var outputBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val saveImageToFile = {
        outputBitmap?.let { bitmap ->
            coroutineScope.launch {
                val filename = imageObject.url.substring(
                    imageObject.url.lastIndexOf(File.separator).plus(1),
                    imageObject.url.lastIndexOf(".") // No file ext
                )
                onSaveImageToFile(bitmap,filename)
            }
        }
    }
    val sheetState = rememberModalBottomSheetState()
    val textHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)
    val rowVerticalPadding = dimensionResource(R.dimen.padding_small)
    val verticalSpacing = dimensionResource(R.dimen.spacing_vertical_mini)

    val composables = mutableListOf<@Composable () -> Unit>()
    composables.add(
        {
            AsyncImage(
                model = ImageRequest.Builder(
                    context = context
                )
                .data(imageObject.url)
                .listener(
                    onError = { _, result ->
                        println(result)
                    },
                    onSuccess = { _, result ->
                        val bitmap = result.drawable.toBitmap()
                        onImageBitmapLoaded(bitmap)
                        outputBitmap = bitmap
                    }
                )
                .build(),
                contentDescription = title,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                    .size(dimensionResource(R.dimen.image_info_avatar_size))
                    .clip(CircleShape)
                    .border(
                        width = dimensionResource(R.dimen.image_info_border_size),
                        color =  colorResource(R.color.image_info_border_color),
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )
        }
    )
    val dimensions = listOf(
        stringResource(
            R.string.image_info_width,
            imageObject.width
        ),
        stringResource(
            R.string.image_info_height,
            imageObject.height
        ),
        imageSizeInBytes.imageSizeFormatted()
    )
    dimensions.forEach { dimension ->
        composables.add(
            {
                Text(
                    text = dimension,
                    modifier = Modifier.padding(
                        horizontal = textHorizontalPadding,
                        vertical = textVerticalPadding
                    )
                )
            }
        )
    }
    val titleText = @Composable {
        Text(
            text = stringResource(
                stringId,
                title
            ),
            modifier = Modifier.padding(
                horizontal = paddingHorizontal,
                vertical = paddingVertical
            )
        )
    }
    composables.add(titleText)

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        sheetState = sheetState
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(verticalSpacing),
            state = lazyListState,
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            item(key = Int.MIN_VALUE) {
                Row(
                    modifier = Modifier.padding(
                        vertical = rowVerticalPadding
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        headerText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(
                                horizontal = textHorizontalPadding
                            )
                            .weight(1F)
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = iconLargePadding)
                            .size(iconSize),
                        onClick = { onDismissRequest() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(
                                R.string.bottom_sheet_dismiss
                            )
                        )
                    }
                }
            }
            itemsIndexed(
                items = composables
            ) { _, composable ->
                composable.invoke()
            }
            item(key = Int.MAX_VALUE) {
                Row(
                    modifier = Modifier.padding(textHorizontalPadding)
                ) {
                    ButtonWithIcon(
                        buttonWidth = dimensionResource(
                            R.dimen.button_width
                        ),
                        drawableId = R.drawable.outline_file_save_24,
                        isEnabled = (outputBitmap != null),
                        onClickButton = { saveImageToFile() },
                        stringId = R.string.bottom_sheet_save_button
                    )
                }
            }
        }
    }
}