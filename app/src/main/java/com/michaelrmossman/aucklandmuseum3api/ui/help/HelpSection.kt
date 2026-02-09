package com.michaelrmossman.aucklandmuseum3api.ui.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.objects.HelpSectionObject
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

@Composable
fun HelpSection(
    helpSection: HelpSectionObject,
    modifier: Modifier = Modifier
) {
    val cardPadding = dimensionResource(R.dimen.padding_small)
    val headerVerticalPadding = dimensionResource(R.dimen.padding_medium)
    val iconVerticalPadding = dimensionResource(R.dimen.padding_mini)
    val rowPadding = dimensionResource(R.dimen.padding_small)
    val textHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val textVerticalPadding = dimensionResource(R.dimen.padding_small)

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(
                R.dimen.card_elevation
            )
        ),
        modifier = modifier.padding(
            horizontal = cardPadding
        ),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        )
    ) {

        Column {
            Text(
                text = helpSection.helpSectionHeader,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = textVerticalPadding,
                        end = textHorizontalPadding,
                        start = textHorizontalPadding,
                        top = headerVerticalPadding
                    )
            )

            for (i in helpSection.helpSectionDrawables.indices) {
                helpSection.helpSectionDrawables[i]?.let { drawable ->
                    Row(
                        modifier = Modifier.padding(rowPadding),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            colorFilter = ColorFilter.tint(
                                colorResource(R.color.icon_surface)
                            ),
                            contentDescription = null,
                            model = drawable,
                            modifier = Modifier
                                .padding(
                                    top = iconVerticalPadding
                                )
                                .size(
                                    dimensionResource(R.dimen.icon_size_small)
                                )
                        )
                        Text(
                            text =
                                helpSection.helpSectionStrings[i].fromHtml(),
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .weight(1F)
                                .padding(
                                    horizontal = textHorizontalPadding
                                )
                        )
                    }
                }
            }
        }
    }
}