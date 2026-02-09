package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.annotation.PluralsRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.ui.components.ItemImage
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.capitalise
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

@Composable
fun PersonValueText(
    @PluralsRes pluralsId: Int,
    result: OpacPerson,
    identifier: String,
    modifier: Modifier = Modifier,
    /* for i.e. Date of Death */
    showIfBlank: Boolean = true,
    pipeSeparator: Boolean = false
) {
    val personValue = result.valuesByIdentifier(
        showIfBlank = showIfBlank,
        identifier = identifier
    )

    if (
        personValue?.any { value -> value.isNotBlank() } == true
        ||
        showIfBlank
    ) {
        val personValueText = getTextFromList(
            capitalise = true,
            list = personValue,
//            ?.map { value ->
//                value.capitalise()
//            },
            pluralsId = pluralsId,
            pipeSeparator = pipeSeparator
        )

        Text(
            text = personValueText,
            modifier = modifier
        )
    }
}