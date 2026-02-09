package com.michaelrmossman.aucklandmuseum3api.ui.objects

import androidx.annotation.PluralsRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

@Composable
fun ObjectValueText(
    identifier: String,
    @PluralsRes pluralsId: Int,
    result: OpacObject,
    modifier: Modifier = Modifier,
    pipeSeparator: Boolean = false
) {
    val objectValues = result.valuesByIdentifier(
        identifier = identifier
    )
    val objectValueText = getTextFromList(
        capitalise = true,
        list = objectValues,
        pluralsId = pluralsId,
        pipeSeparator = pipeSeparator
    )

    Text(
        text = objectValueText,
        modifier = modifier
    )
}