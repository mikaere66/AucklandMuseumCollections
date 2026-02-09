package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

/* Only named as such so that it appears in
   file lists before RelatedBottomSheet */
@Composable
fun RelatedArtefacts(
    relatedRecords: List<Triple<String, String, String>>,
    /* Modifier used by all Text items */
    modifier: Modifier = Modifier
) {
    val relatedText = pluralStringResource(
        R.plurals.details_related,
        relatedRecords.size,
        relatedRecords.size
    ).fromHtml()
    var showRelatedBS by rememberSaveable { mutableStateOf(false) }

    if (showRelatedBS) {
        RelatedBottomSheet(
            headerText = relatedText,
            relatedRecords = relatedRecords,
            onDismissRequest = { showRelatedBS = false }
        )
    }

    ReferenceTextWithIcon(
        isEnabled = relatedRecords.isNotEmpty(),
        modifier = modifier,
        onClickReferences = { showRelatedBS = true },
        referenceText = relatedText
    )
}