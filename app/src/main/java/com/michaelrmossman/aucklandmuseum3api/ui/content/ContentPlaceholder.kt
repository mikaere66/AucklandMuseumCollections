package com.michaelrmossman.aucklandmuseum3api.ui.content

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.LayoutDirection
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.ui.InitContent
import com.michaelrmossman.aucklandmuseum3api.ui.components.SingleLineAppBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ContentPlaceholder(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    mediaType: MediaType? = null
) {
    val additionalPadding = dimensionResource(R.dimen.padding_small)
    val cardCornerShape = dimensionResource(R.dimen.card_corner_shape)
    val cardElevation = dimensionResource(R.dimen.card_elevation)

    Scaffold(
        topBar = {
            SingleLineAppBar(stringId = 0)
        },
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) { contentPadding ->

        Card(
            modifier = modifier.padding(
                top = contentPadding.calculateTopPadding().plus(
                    additionalPadding
                ),
                end = contentPadding.calculateEndPadding(
                    LayoutDirection.Ltr
                ).plus(additionalPadding),
                bottom = contentPadding.calculateBottomPadding().plus(
                    additionalPadding
                )
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = cardElevation
            ),
            shape = RoundedCornerShape(size = cardCornerShape)
        ) {
            InitContent(
                drawableId = drawableId,
                mediaType = mediaType,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                stringId = when (mediaType) {
                    MediaType.Object -> {
                        R.string.details_message_coll_objects
                    }
                    MediaType.Person -> {
                        R.string.details_message_coll_persons
                    }
                    else -> R.string.details_message_init_faves
                }
            )
        }
    }
}