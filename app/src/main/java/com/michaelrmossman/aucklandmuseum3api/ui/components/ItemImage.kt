package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.toSecureUrl
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.fontDimensionResource

@Composable
fun ItemImage(
    onClickImage: () -> Unit,
    url: String,
    modifier: Modifier = Modifier,
    titleText: AnnotatedString? = null
) {
    val context = LocalContext.current
    val copyrightFontSize = fontDimensionResource(R.dimen.font_size_copyright)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_micro)
        ),
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClickImage()
            }
    ) {
        AsyncImage(
            contentDescription = stringResource(R.string.museum_front_desc),
            model = ImageRequest.Builder(
                context = context
            )
            .data(url.toSecureUrl())
            .crossfade(true)
            .build(),
            modifier = modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.padding_micro),
                    vertical = dimensionResource(R.dimen.padding_mini)
                )
        )

        titleText?.let { title ->
            Text(
                fontSize = copyrightFontSize,
                text = title,
                textAlign = TextAlign.Center
            )
        }
    }
}