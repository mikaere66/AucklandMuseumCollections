package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

/* Only named as such so that it appears in
   file lists before MultiBottomSheet() */
@Composable
fun MultiArtefacts(
    identifier: String,
    @PluralsRes pluralsId: Int,
    result: OpacObject,
    /* Modifier used by all Text items */
    modifier: Modifier = Modifier
) {
    val objectValues = result.valuesByIdentifier(
        identifier = identifier
    )?.filter { objectValue ->
        objectValue.isNotBlank()
    }
    val titleText = pluralStringResource(
        pluralsId,
        objectValues?.size ?: 0,
        objectValues?.size ?: 0
    ).fromHtml()
    var showMultiBS by rememberSaveable { mutableStateOf(false) }

    if (showMultiBS) {
        objectValues?.let { values ->
            MultiBottomSheet(
                headerText = titleText,
                modifier = modifier,
                onDismissRequest = { showMultiBS = false },
                values = values
            )
        }
    }

    ReferenceTextWithIcon(
        isEnabled = objectValues?.isNotEmpty() == true,
        modifier = modifier,
        onClickReferences = { showMultiBS = true },
        referenceText = titleText
    )
}