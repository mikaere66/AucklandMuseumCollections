package com.michaelrmossman.aucklandmuseum3api.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.VpnKeyOff
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.ui.components.MessageWithIcon
import com.michaelrmossman.aucklandmuseum3api.ui.theme.AucklandMuseumV3APITheme
import com.michaelrmossman.aucklandmuseum3api.util.ColorUtils.getEmptyListColor
import com.michaelrmossman.aucklandmuseum3api.util.IconUtils.getCollectionIconId
import com.michaelrmossman.aucklandmuseum3api.util.ModifierUtils.downloading
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.fontDimensionResource
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

@Composable
fun EmptyFaves(
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
//    val color = colorResource(R.color.empty_list)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawableId),
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.icon_size_giant)
            ),
            colorFilter = ColorFilter.tint(
                color = getEmptyListColor(),
                blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = stringResource(stringId),
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Composable
fun EmptySearchScreen(
    query: String,
    modifier: Modifier = Modifier
) {
//    val color = colorResource(R.color.empty_list)
    val message1 = stringResource(
        R.string.empty_list,
        query
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.icon_size_giant)
            ),
            colorFilter = ColorFilter.tint(
                color = getEmptyListColor(),
                blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message1.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Composable
fun ErrorScreen(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
//    val color = colorResource(R.color.empty_list)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.icon_size_giant)
            ),
            colorFilter = ColorFilter.tint(
                color = getEmptyListColor(),
                blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = stringResource(R.string.loading_failed),
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_large)
            )
        )
        Button(onClick = retryAction) {
            Text(stringResource(R.string.loading_retry))
        }
        errorMessage?.let { message ->
            Text(
                fontSize = fontDimensionResource(
                    R.dimen.font_size_error
                ),
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    dimensionResource(R.dimen.padding_large)
                )
            )
        }
    }
}

@Composable
fun ForbiddenScreen(
    modifier: Modifier = Modifier
) {
//    val color = colorResource(R.color.empty_list)
    val message = stringResource(
        R.string.forbidden_api_key
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.VpnKeyOff,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.icon_size_giant)
            ),
            colorFilter = ColorFilter.tint(
                color = getEmptyListColor(),
                blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Composable
fun InitScreen(
    @DrawableRes drawableId: Int,
    mediaType: MediaType,
    @StringRes stringId: Int,
    windowHeightSize: WindowHeightSizeClass,
    windowWidthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    currentCollection: Collection? = null
) {
    if (
        windowHeightSize == WindowHeightSizeClass.Compact
        ||
        windowWidthSize == WindowWidthSizeClass.Compact
    ) {
        InitContent(
            currentCollection = currentCollection,
            drawableId = drawableId,
            mediaType = mediaType,
            modifier = modifier,
            stringId = stringId
        )
    }
}
@Composable
fun InitContent(
    @DrawableRes drawableId: Int,
    mediaType: MediaType?,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier,
    currentCollection: Collection? = null
) {
//    val color = colorResource(R.color.empty_list)
    val message = stringResource(stringId)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawableId),
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.icon_size_giant)
            ),
            colorFilter = ColorFilter.tint(
                color = getEmptyListColor(),
                blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
        if (mediaType == MediaType.Object) {
            MessageWithIcon(
                drawableId = R.drawable.outline_manage_search_24,
                stringId = R.string.details_message_manage_search
            )
            MessageWithIcon(
                drawableId = when (currentCollection) {
                    null -> R.drawable.outline_more_vert_24
                    else -> getCollectionIconId(currentCollection)
                },
                stringId = R.string.details_message_advanced_search
            )
        }
    }
}

@Composable
fun LoadingScreen(
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            contentDescription = stringResource(R.string.loading_anim),
            painter = painterResource(R.drawable.loading_image),
            modifier = Modifier
                .size(
                    dimensionResource(R.dimen.loading_anim_large)
                )
                /* Refer custom modifier in ModifierUtils */
                .downloading(isDownloading = true)
        )
        Text(
            text = stringResource(stringId),
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_large)
            )
        )
    }
}

@Composable
fun NotFoundScreen(
    query: String,
    modifier: Modifier = Modifier
) {
//    val color = colorResource(R.color.empty_list)
    val message = stringResource(
        R.string.found_not_message,
        query
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Outlined.WarningAmber,
            contentDescription = null,
            modifier = Modifier.size(
                dimensionResource(R.dimen.icon_size_giant)
            ),
            colorFilter = ColorFilter.tint(
                color = getEmptyListColor(),
                blendMode = BlendMode.SrcIn
            )
        )
        Text(
            text = message.fromHtml(),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                dimensionResource(R.dimen.padding_mega)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptySearchPreview() {
    AucklandMuseumV3APITheme {
        EmptySearchScreen(
            query = "Harry Houdini"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    AucklandMuseumV3APITheme {
        ErrorScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun ForbiddenScreenPreview() {
    AucklandMuseumV3APITheme {
        ForbiddenScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun InitScreenPreview() {
    AucklandMuseumV3APITheme {
        InitScreen(
            drawableId = R.drawable.outline_category_24,
            mediaType = MediaType.Object,
            stringId = R.string.details_message_coll_objects,
            windowHeightSize = WindowHeightSizeClass.Medium,
            windowWidthSize = WindowWidthSizeClass.Compact
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    AucklandMuseumV3APITheme {
        LoadingScreen(
            stringId = R.string.loading_anim
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotFoundScreenPreview() {
    AucklandMuseumV3APITheme {
        NotFoundScreen(
            query = "rhinoceros"
        )
    }
}