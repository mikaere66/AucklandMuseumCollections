package com.michaelrmossman.aucklandmuseum3api.ui.components

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
import com.michaelrmossman.aucklandmuseum3api.util.capitalise

@Composable
fun MultiCard(
    value: String,
    paddingHorizontal: Dp,
    paddingVertical: Dp,
    /* Modifier used by Text items */
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        )
    ) {
        Row(
            modifier = Modifier.padding(
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
                text = value.trim().capitalise(),
                modifier = modifier
            )
        }
    }
}