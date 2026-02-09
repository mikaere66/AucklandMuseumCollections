package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObject
import com.michaelrmossman.aucklandmuseum3api.ui.components.RelatedArtefacts
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_DATE_OF_BIRTH
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_DATE_OF_DEATH
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_INDIVIDUAL
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_PERSON_CORP_TYPE
import com.michaelrmossman.aucklandmuseum3api.util.relatedRecords
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

/* This card used by both DetailsPager and ImagesPager */
@Composable
fun PersonDetailsCard(
    contentPadding: PaddingValues,
    onClickImage: (Int, List<ImageObject>) -> Unit,
    result: OpacPerson,
    titlesFlattened: String,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [OpacPerson]s */
    modifier: Modifier = Modifier,
    fromFavourites: Boolean = false
) {
    val cardCornerShape = dimensionResource(R.dimen.card_corner_shape)
    val cardElevation = dimensionResource(R.dimen.card_elevation)
    val cardHorizontalPadding = dimensionResource(
        R.dimen.card_horizontal_padding
    )
    val cardVerticalPadding = dimensionResource(R.dimen.padding_small)
    val columnHorizontalPadding = dimensionResource(R.dimen.padding_small)
    val columnVerticalPadding = dimensionResource(R.dimen.padding_small)
    val columnVerticalSpacing = dimensionResource(
        R.dimen.spacing_vertical_small
    )
    val scrollState = rememberScrollState()
    val searchBoxPaddingBottom = contentPadding.calculateBottomPadding()
    val searchBoxPaddingTop = when (
        fromFavourites
        ||
        windowSize == WindowWidthSizeClass.Compact
    ) {
        true -> 0.dp
        else -> contentPadding.calculateTopPadding()
    }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation
        ),
        shape = RoundedCornerShape(size = cardCornerShape),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = searchBoxPaddingBottom.plus(
                    cardVerticalPadding
                ),
                end = cardHorizontalPadding,
                start = cardHorizontalPadding,
                top = searchBoxPaddingTop.plus(
                    cardVerticalPadding
                )
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = columnHorizontalPadding,
                    vertical = columnVerticalPadding
                )
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(
                columnVerticalSpacing
            )
        ) {

            /* Also used by ListItemCard() */
            PersonListItemContent(
                result = result,
                titlesFlattened = titlesFlattened,
                /* Modifier used by all [OpacPerson]s */
                modifier = modifier
            )

            val personOrCorp = result.valuesByIdentifier(
                IDENTIFIER_PERSON_CORP_TYPE
            )
            val individual = (
                personOrCorp?.contains(IDENTIFIER_INDIVIDUAL) == true
            )
            PersonValueText(
                identifier = IDENTIFIER_DATE_OF_BIRTH,
                pluralsId = when (individual) {
                    true -> R.plurals.details_date_birth
                    else -> R.plurals.details_date_opened
                },
                result = result
            )

            PersonValueText(
                identifier = IDENTIFIER_DATE_OF_DEATH,
                pluralsId = when (individual) {
                    true -> R.plurals.details_date_death
                    else -> R.plurals.details_date_closed
                },
                result = result,
                showIfBlank = false
            )

            RelatedArtefacts(
                modifier = modifier,
                relatedRecords =
                    result.relationshipsCollection.relatedRecords()
            )

            PersonImages(
                onClickImage = onClickImage,
                result = result
            )
        }
    }
}