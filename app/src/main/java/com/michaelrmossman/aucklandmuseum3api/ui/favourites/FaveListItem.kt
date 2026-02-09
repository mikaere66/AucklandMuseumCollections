package com.michaelrmossman.aucklandmuseum3api.ui.favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.data.FaveEntity
import com.michaelrmossman.aucklandmuseum3api.enum.MediaType
import com.michaelrmossman.aucklandmuseum3api.navigation.CurrentScreen
import com.michaelrmossman.aucklandmuseum3api.ui.components.FaveIcon
import com.michaelrmossman.aucklandmuseum3api.ui.components.TypeIcon
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_RECORD
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromString
import com.michaelrmossman.aucklandmuseum3api.util.parseMillisToKiwiDate

@Composable
fun FaveListItem(
    fave: FaveEntity,
    onClickToggleFavourite: () -> Unit,
    onClickFavourite: (FaveEntity, MediaType) -> Unit,
    // onLongClickFavourite: (AMapMarker) -> Unit,
    /* Modifier used by all [Text] composables */
    modifier: Modifier = Modifier
) {
    val columnHorizontalPadding = dimensionResource(R.dimen.padding_medium)
    val columnVerticalPadding = dimensionResource(R.dimen.padding_mini)
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_mini
    )

    val titleText = fave.title
    val subtitleText = fave.subtitle

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        onClick = {
            onClickFavourite(fave, MediaType.valueOf(fave.media))
        },
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        )
    ) {

        Column(
            modifier = Modifier.padding(
                bottom = columnVerticalPadding,
                end = columnHorizontalPadding,
                start = columnHorizontalPadding
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                columnVerticalSpacing
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.spacing_horizontal_midi)
                ),
            ) {
                TypeIcon(
                    drawableId = when (MediaType.valueOf(fave.media)) {
                        MediaType.Object -> {
                            CurrentScreen.ObjectsSearch.drawableId
                        }
                        MediaType.Person -> {
                            CurrentScreen.PersonsSearch.drawableId
                        }
                    }
                )
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.weight(1F)
                )
                FaveIcon(
                    isFave = fave.isFave,
                    onClickToggleFavourite = onClickToggleFavourite
                )
            }
            Text(
                text = getTextFromList(
                    capitalise = (fave.media == MediaType.Person.name),
                    list = subtitleText.split(
                        SEPARATOR_RECORD
                    ),
                    pluralsId = when (MediaType.valueOf(fave.media)) {
                        MediaType.Object -> R.plurals.list_item_departments
                        MediaType.Person -> R.plurals.list_item_types
                    },
                    pipeSeparator = (fave.media == MediaType.Object.name)
                ),
                modifier = modifier
            )
            if (fave.added != 0L) {
                Text(
                    text = getTextFromString(
                        stringId = R.string.faves_added,
                        string = fave.added.parseMillisToKiwiDate()
                    ),
                    modifier = modifier
                )
            }
        }
    }
}