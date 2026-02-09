package com.michaelrmossman.aucklandmuseum3api.ui.objects

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObject
import com.michaelrmossman.aucklandmuseum3api.ui.components.RelatedArtefacts
import com.michaelrmossman.aucklandmuseum3api.ui.components.MultiArtefacts
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_ASSOC_EVENT
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_ASSOC_NOTES
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_ASSOC_PLACE
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_COPYRIGHT
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_DESCRIPTION
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_DOCUMENTATION
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_LAST_UPDATE
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_OBJECT_TYPE
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_SUBJECT_CATEGORY
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_TAXONOMIC
import com.michaelrmossman.aucklandmuseum3api.util.relatedRecords

/* This card used by both DetailsPager and ImagesPager */
@Composable
fun ObjectDetailsCard(
    contentPadding: PaddingValues,
    onClickImage: (Int, List<ImageObject>) -> Unit,
    result: OpacObject,
    titlesFlattened: String,
    windowSize: WindowWidthSizeClass,
    /* Modifier used by all [OpacObject]s */
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
            ObjectListItemContent(
                fullText = true,
                result = result,
                titlesFlattened = titlesFlattened,
                /* Modifier used by all [OpacObject]s */
                modifier = modifier
            )

            /* Group these to avoid additional padding,
               as [IconButton]s have 48.dp overall */
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                MultiArtefacts(
                    identifier = IDENTIFIER_ASSOC_EVENT,
                    pluralsId = R.plurals.details_assoc_events,
                    result = result,
                )
                MultiArtefacts(
                    identifier = IDENTIFIER_ASSOC_NOTES,
                    pluralsId = R.plurals.details_assoc_notes,
                    result = result,
                )
                MultiArtefacts(
                    identifier = IDENTIFIER_ASSOC_PLACE,
                    pluralsId = R.plurals.details_assoc_places,
                    result = result,
                )
                MultiArtefacts(
                    identifier = IDENTIFIER_TAXONOMIC,
                    pluralsId = R.plurals.details_taxonomic,
                    result = result,
                    /* Modifier used by all Text items */
                    // modifier = modifier
                )
            }

            ObjectValueText(
                identifier = IDENTIFIER_OBJECT_TYPE,
                modifier = modifier,
                pluralsId = R.plurals.list_item_types,
                result = result
            )

            ObjectValueText(
                identifier = IDENTIFIER_SUBJECT_CATEGORY,
                modifier = modifier,
                pluralsId = R.plurals.details_categories,
                result = result,
                pipeSeparator = true
            )

            ObjectMultiText(
                identifier = IDENTIFIER_DESCRIPTION,
                result = result,
                title = stringResource(R.string.details_description)
            )

            ObjectMultiText(
                identifier = IDENTIFIER_DOCUMENTATION,
                result = result,
                title = stringResource(R.string.details_documentation)
            )

            RelatedArtefacts(
                modifier = modifier,
                relatedRecords =
                    result.relationshipsCollection.relatedRecords()
            )

            ObjectImages(
                onClickImage = onClickImage,
                result = result
            )

            ObjectValueText(
                identifier = IDENTIFIER_COPYRIGHT,
                modifier = modifier,
                pluralsId = R.plurals.details_copyrights,
                result = result
            )

            ObjectValueText(
                identifier = IDENTIFIER_LAST_UPDATE,
                pluralsId = R.plurals.details_updates,
                result = result
            )
        }
    }
}