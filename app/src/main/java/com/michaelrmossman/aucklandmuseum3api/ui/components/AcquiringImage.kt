package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.ModifierUtils.downloading

@Suppress("KotlinConstantConditions") /* isDownloading | isError */
@Composable
fun AcquiringImage(
    isDownloading: Boolean,
    isError: Boolean,
    onDownloadClick: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isDownloading) {
            Image(
                contentDescription = stringResource(
                    R.string.loading_anim
                ),
                painter = painterResource(
                    R.drawable.loading_image
                ),
                modifier = modifier
                    .size(
                        dimensionResource(
                            R.dimen.loading_anim_large
                        )
                    )
                    /* Refer custom modifier in ModifierUtils */
                    .downloading(isDownloading = true)
            )
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.loading_anim),
                textAlign = TextAlign.Center
            )

        } else if (isError) {

            RetryDownload(
                arrangementHorizontal = Arrangement.Center,
                drawableId = R.drawable.outline_broken_image_24,
                isDownloading = isDownloading,
                isError = isError,
                onDownloadClick = onDownloadClick,
                stringId = stringId
            )
        }
    }
}