package com.michaelrmossman.aucklandmuseum3api.ui.images

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObject
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObjects
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.ui.components.AcquiringImage
import com.michaelrmossman.aucklandmuseum3api.ui.components.BackButton
import com.michaelrmossman.aucklandmuseum3api.ui.components.ImageInfoBottomSheet
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.fontDimensionResource

/* Multiple images */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
    images: ImageObjects,
    onClickBackButton: () -> Unit,
    onSaveImageToFile: (Bitmap, String) -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val additionalPadding = dimensionResource(R.dimen.padding_mini)
    val context = LocalContext.current
    var isDownloading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current
    val lazyListState = rememberLazyListState()
    val paddingHorizontal = dimensionResource(R.dimen.padding_medium)
    val paddingVertical = dimensionResource(R.dimen.padding_small)
    val pluralsId = when (images.mediaType) {
        MediaType.Object -> R.plurals.nav_images_1
        MediaType.Person -> R.plurals.nav_images_2
    }
    val roundedCornerShape = dimensionResource(R.dimen.card_corner_shape)
    var showInfoBottomSheet by remember { mutableStateOf<ImageObject?>(null) }
    val subtitleFontSize = fontDimensionResource(R.dimen.font_size_subtitle)
    val subtitle = stringResource(
        stringId,
        images.itemTitle
    )
    /* This is the title shown in the TopAppBar.
       Title for bottomSheet is object's name */
    val title = pluralStringResource(
        pluralsId,
        images.imageObjects.size,
        images.imageObjects.size
    )

    if (showInfoBottomSheet != null) {
        showInfoBottomSheet?.let { imageObject ->
            ImageInfoBottomSheet(
                imageObject = imageObject,
                onDismissRequest = { showInfoBottomSheet = null },
                onSaveImageToFile = onSaveImageToFile,
                paddingHorizontal = paddingHorizontal,
                paddingVertical = paddingVertical,
                stringId = stringId,
                title = images.itemTitle
            )
        }
    }

    /* LaunchedEffect at EOF for scrollToItem() */

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    BackButton(onClickBackButton = onClickBackButton)
                },
                title = {
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            fontSize = subtitleFontSize,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = subtitle
                        )
                    }
                }
            )
        }
    ) { contentPadding ->

        Box (
            modifier = modifier
                .fillMaxSize()
                .padding(
                    bottom = contentPadding.calculateBottomPadding().plus(
                        additionalPadding
                    ),
                    end = contentPadding.calculateEndPadding(
                        layoutDirection
                    ),
                    start = contentPadding.calculateStartPadding(
                        layoutDirection
                    ),
                    top = contentPadding.calculateTopPadding().plus(
                        additionalPadding
                    )
                )
        ) {

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_vertical_mini)
                )
            ) {
                if (
                    !isError
                    ||
                    isDownloading
                ) {
                    itemsIndexed(
                        items = images.imageObjects,
                    ) { index, imageObject ->

                        Card(
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = dimensionResource(
                                    R.dimen.card_elevation
                                )
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showInfoBottomSheet = imageObject
                            },
                            shape = RoundedCornerShape(roundedCornerShape)
                        ) {
                            AsyncImage(
                                contentDescription = images.itemTitle,
                                model = ImageRequest.Builder(
                                    context = context
                                )
                                .data(imageObject.url)
                                .crossfade(true)
                                .listener(
                                    onError = { _, _ ->
                                        isDownloading = false
                                        isError = true
                                    },
                                    onSuccess = { _, _ ->
                                        isDownloading = false
                                        isError = false
                                    }
                                )
                                .build(),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = paddingVertical,
                                        vertical = paddingVertical
                                    )
                                    .clip(RoundedCornerShape(
                                        size = roundedCornerShape
                                    ))
                            )
                        }
                    }
                }

                if (isDownloading || isError) {
                    item(key = -1) {
                        AcquiringImage(
                            isDownloading = isDownloading,
                            isError = isError,
                            modifier = Modifier.fillMaxSize(),
                            onDownloadClick = {
                                isDownloading = true
                                isError = false
                            }, /* Note use of images plural */
                            stringId = R.string.images_retry
                        )
                    }
                }
            }

            LaunchedEffect(key1 = Unit) {
                if (images.itemIndex > 0) {
                    lazyListState.scrollToItem(images.itemIndex)
                }
            }
        }
    }
}