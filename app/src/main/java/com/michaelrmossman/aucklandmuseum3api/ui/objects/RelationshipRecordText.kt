package com.michaelrmossman.aucklandmuseum3api.ui.objects

import androidx.annotation.PluralsRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

@Composable
fun RelationshipRecordText(
    @PluralsRes pluralsId: Int,
    value: String,
    result: OpacObject,
    modifier: Modifier = Modifier,
    pipeSeparator: Boolean = false
) {
    val objectValue = result.valuesByIdentifier(value)
    val objectValueText = getTextFromList(
        capitalise = true,
        list = objectValue,
        pluralsId = pluralsId,
        pipeSeparator = pipeSeparator
    )

    Text(
        text = objectValueText,
        modifier = modifier
    )
}