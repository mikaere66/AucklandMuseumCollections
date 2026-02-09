package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.DOWNLOAD_COMPLETE_DELAY
import com.michaelrmossman.aucklandmuseum3api.util.ModifierUtils.downloading
import com.michaelrmossman.aucklandmuseum3api.util.ResourceUtils.downloadDrawableIds
import com.michaelrmossman.aucklandmuseum3api.util.ResourceUtils.downloadStringIds
import kotlinx.coroutines.delay

/* Used on appBar in [ResultDetailsScreen] */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DownloadButton(
    canDownloadMore: Boolean,
    isDownloading: Boolean,
    isEnabled: Boolean,
    onClickDownloadButton: () -> Unit,
    onDownloadComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var downloadComplete by remember { mutableStateOf(true) }
    val showCompleteIcon by remember { /* Must be remember */
        derivedStateOf { !downloadComplete && !isDownloading }
    }

    /* When download button is clicked, downloadComplete becomes false
       and isDownloading becomes true (elsewhere). When isDownloading
       returns to false, it means that downloading is indeed complete.
       Show a different icon briefly, then reset downloadComplete */
    LaunchedEffect(key1 = showCompleteIcon) {
        onDownloadComplete()

        if (showCompleteIcon) {
            delay(DOWNLOAD_COMPLETE_DELAY) // 2250ms
            downloadComplete = true
        }
    }

    IconButton(
        enabled = isEnabled,
        onClick = {
            onClickDownloadButton()
            downloadComplete = false
        }
    ) {
        Icon(
            contentDescription = stringResource(when (isDownloading) {
                true -> downloadStringIds.first
                else -> when (showCompleteIcon) {
                    true -> R.string.download_desc
                    else -> when (canDownloadMore) {
                        true -> downloadStringIds.second
                        else -> downloadStringIds.third
                    }
                }
            }),
            /* Refer custom modifier in ModifierUtils */
            modifier = modifier.downloading(isDownloading),
            painter = painterResource(when (isDownloading) {
                true -> downloadDrawableIds.first
                else -> when (showCompleteIcon) {
                    true -> R.drawable.outline_download_done_24
                    else -> when (canDownloadMore) {
                        true -> downloadDrawableIds.second
                        else -> downloadDrawableIds.third
                    }
                }
            })
        )
    }
}