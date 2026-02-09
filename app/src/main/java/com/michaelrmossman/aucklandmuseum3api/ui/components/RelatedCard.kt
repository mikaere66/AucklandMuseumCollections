package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_VALUE_UNKNOWN
import com.michaelrmossman.aucklandmuseum3api.util.capitalise

@Composable
fun RelatedCard(
    paddingHorizontal: Dp,
    paddingVertical: Dp,
    onRelatedClick: () -> Unit,
    recordType: String,
    relatedId: String,
    title: String,
    /* Modifier used by Text items */
    modifier: Modifier = Modifier
) {
    val isEnabled = (
        relatedId.isNotBlank()
        &&
        relatedId != IDENTIFIER_VALUE_UNKNOWN
        &&
        title != IDENTIFIER_VALUE_UNKNOWN
    )
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        )
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    enabled = isEnabled
                ) {
                    onRelatedClick()
                }
                .padding(
                    end = paddingHorizontal,
                    start = paddingHorizontal,
                    top = paddingVertical
                ),
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title.capitalise(),
                modifier = modifier
            )
            TypeIcon(
                drawableId = when (
                    MediaType.valueOf(recordType.capitalise())
                ) {
                    MediaType.Person -> when (isEnabled) {
                        true -> R.drawable.baseline_person_24
                        else -> R.drawable.outline_person_24
                    }
                    else -> when (isEnabled) {
                        true -> R.drawable.baseline_category_24
                        else -> R.drawable.outline_category_24
                    }
                },
                modifier = Modifier.padding(
                    end = paddingHorizontal,
                    start = paddingHorizontal,
                    bottom = paddingVertical
                )
            )
        }
    }
}