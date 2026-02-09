package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.ModifierUtils.downloading

@Composable
fun RetryDownload(
    arrangementHorizontal: Arrangement.Horizontal,
    @DrawableRes drawableId: Int,
    isDownloading: Boolean,
    isError: Boolean,
    onDownloadClick: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val downloadText = when (isDownloading) {
        true -> String()
        else -> stringResource(stringId)
    }

    Row(
        horizontalArrangement = arrangementHorizontal,
        modifier = modifier.clickable(
            enabled = !isDownloading
        ) {
            onDownloadClick()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            enabled = !isDownloading,
            onClick = onDownloadClick
        ) {
            Icon(
                contentDescription = null,
                painter = painterResource(
                    when (isDownloading) {
                        true -> R.drawable.loading_image
                        else -> drawableId
                    }
                ),
                /* Refer custom modifier in ModifierUtils */
                modifier = Modifier.downloading(isDownloading),
                tint = when (isError) {
                    true -> Color.Red
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
        Text(text = downloadText)
    }
}