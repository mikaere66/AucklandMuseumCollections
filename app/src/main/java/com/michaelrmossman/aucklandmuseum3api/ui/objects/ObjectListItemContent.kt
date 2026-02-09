package com.michaelrmossman.aucklandmuseum3api.ui.objects

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_DEPARTMENT
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_TYPE_TITLE
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_ITEM
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromString
import com.michaelrmossman.aucklandmuseum3api.util.subtitleText
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

@Composable
fun ObjectListItemContent(
    fullText: Boolean,
    result: OpacObject,
    titlesFlattened: String,
    /* Modifier used by all [OpacObject]s */
    modifier: Modifier = Modifier
) {
    val titleText = getTextFromString(
        stringId = R.string.list_item_title,
        string = titlesFlattened
    )
    val titleMaxLines = when (fullText) {
        true -> Int.MAX_VALUE // Details
        else -> integerResource( // List
            id = R.integer.summary_max_lines
        )
    }

    val departments = result.subtitleText()
    val departmentsText = getTextFromList(
        list = departments,
        pluralsId = R.plurals.list_item_departments,
        pipeSeparator = true
    )

    Text(
        text = titleText,
        maxLines = titleMaxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
    Text(
        text = departmentsText,
        modifier = modifier
    )
}