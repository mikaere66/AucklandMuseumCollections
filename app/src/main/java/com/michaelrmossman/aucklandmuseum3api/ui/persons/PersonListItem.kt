package com.michaelrmossman.aucklandmuseum3api.ui.persons

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.google.android.gms.maps.model.LatLng
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_ITEM
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromString
import com.michaelrmossman.aucklandmuseum3api.util.capitalise
import com.michaelrmossman.aucklandmuseum3api.util.titleText

@Composable
fun PersonListItem(
    onClickSearchResult: (OpacPerson) -> Unit,
    paddingHorizontal: Dp,
    paddingVertical: Dp,
    result: OpacPerson,
    /* Modifier used by all [OpacPerson]s */
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.card_elevation)
        ),
        onClick = { onClickSearchResult(result) },
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.card_corner_shape)
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                end = paddingHorizontal,
                start = paddingHorizontal,
                top = paddingVertical
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            )
        ) {
            val titlesFlattened = result.titleText()
            PersonListItemContent(
                result = result,
                titlesFlattened = titlesFlattened,
                /* Modifier used by all [OpacPerson]s */
                modifier = modifier
            )
        }
    }
}