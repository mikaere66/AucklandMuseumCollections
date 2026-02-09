package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_NAME_FIRST_LAST
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_PERSON_CORP_TYPE
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_ITEM
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromList
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromString
import com.michaelrmossman.aucklandmuseum3api.util.capitalise
import com.michaelrmossman.aucklandmuseum3api.util.subtitleText
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

@Composable
fun PersonListItemContent(
    result: OpacPerson,
    titlesFlattened: String,
    /* Modifier used by all [OpacPerson]s */
    modifier: Modifier = Modifier
) {
//    val titles = result.valuesByIdentifier(IDENTIFIER_NAME_FIRST_LAST)
    // val titlesFlattened = result.titleText()
//    titles?.joinToString(
//        SEPARATOR_ITEM
//    )

    val titleText = getTextFromString(
        stringId = R.string.list_item_title,
        string = titlesFlattened
    )

    val personOrCorp = result.subtitleText()
//    result.valuesByIdentifier(IDENTIFIER_PERSON_CORP_TYPE)
    val personOrCorpText = getTextFromList(
        capitalise = true,
        list = personOrCorp,
//        ?.map { type ->
//            type.capitalise()
//        },
        pluralsId = R.plurals.list_item_types
    )

    Text(
        text = titleText,
        modifier = modifier
    )
    Text(
        text = personOrCorpText,
        modifier = modifier
    )
}