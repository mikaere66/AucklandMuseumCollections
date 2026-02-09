package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.util.ModifierUtils.downloading
import com.michaelrmossman.aucklandmuseum3api.util.formatWithComma
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

@Suppress("KotlinConstantConditions") /* isDownloading | isError */
@Composable
fun AcquiringStatus(
    isDownloading: Boolean,
    isError: Boolean,
    itemCount: Int,
    mediaType: MediaType,
    onDownloadClick: () -> Unit,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    if (isDownloading) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                contentDescription = stringResource(
                    R.string.loading_anim
                ),
                painter = painterResource(
                    R.drawable.loading_image
                ),
                modifier = Modifier
                    .size(
                        dimensionResource(
                            R.dimen.loading_anim_small
                        )
                    )
                    /* Refer custom modifier in ModifierUtils */
                    .downloading(isDownloading = true)
            )
            Text(
                modifier = Modifier.weight(1F),
                text = stringResource(R.string.loading_anim)
            )
        }

    } else if (isError) {

        RetryDownload(
            arrangementHorizontal = Arrangement.Start,
            drawableId = R.drawable.outline_cloud_download_24,
            isDownloading = isDownloading,
            isError = isError,
            onDownloadClick = onDownloadClick,
            stringId = R.string.bottom_sheet_retry
        )

    } else {

        Text(
            modifier = modifier.padding(
                vertical = dimensionResource(R.dimen.padding_medium)
            ),
            textAlign = textAlign,
            text = stringResource(
                when (mediaType) {
                    MediaType.Object -> R.string.bottom_sheet_objects
                    MediaType.Person -> R.string.bottom_sheet_persons
                },
                itemCount.formatWithComma()
            ).fromHtml()
        )
    }
}