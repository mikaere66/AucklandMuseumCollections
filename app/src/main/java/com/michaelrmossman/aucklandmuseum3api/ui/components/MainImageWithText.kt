package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.fontDimensionResource
import kotlin.math.abs

@Composable
fun MainImageWithText(
    @StringRes stringId: Int,
    windowWidthSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val alignmentBias = -0.975F /* Note negative value */
    val context = LocalContext.current
    val copyrightFontSize = fontDimensionResource(R.dimen.font_size_copyright)
    val imageCornerShape = dimensionResource(R.dimen.image_main_corner_shape)
    val imageTextColor = colorResource(R.color.image_main_text_color)
    val paddingHorizontal = dimensionResource(R.dimen.padding_small)
    val paddingImageVertical = dimensionResource(R.dimen.padding_medium)
    val paddingTextVertical = dimensionResource(R.dimen.padding_vertical_main)

    Box(
        /* Modifier sent from MainScreen */
        modifier = modifier
    ) {
        AsyncImage(
            contentDescription = stringResource(R.string.museum_front_desc),
            model = ImageRequest.Builder(
                context = context
            )
            .data(R.drawable.north_entrance)
            .crossfade(true)
            .build(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = paddingHorizontal,
                    start = paddingHorizontal,
                    top = paddingImageVertical
                )
                /* clip must come last */
                .clip(RoundedCornerShape(
                    topEnd = imageCornerShape,
                    topStart = imageCornerShape
                ))
        )
        Text(
            color = imageTextColor,
            modifier = Modifier
                .align(
                    when (windowWidthSize) {
                        WindowWidthSizeClass.Compact -> {
                            BiasAlignment(0.0F,alignmentBias)
                        }
                        else -> Alignment.TopCenter
                    }
                )
                .padding(
                    vertical = paddingTextVertical
                ),
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(stringId)
        )
        Text(
            color = imageTextColor,
            fontSize = copyrightFontSize,
            modifier = Modifier.align(
                when (windowWidthSize) {
                    WindowWidthSizeClass.Compact -> {
                        BiasAlignment(0.0F,abs(alignmentBias))
                    }
                    else -> Alignment.BottomCenter
                }
            ),
            text = stringResource(R.string.museum_image_copyright)
        )
    }
}