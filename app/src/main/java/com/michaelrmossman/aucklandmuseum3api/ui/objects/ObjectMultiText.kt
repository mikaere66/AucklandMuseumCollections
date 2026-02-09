package com.michaelrmossman.aucklandmuseum3api.ui.objects

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.util.capitalise
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml
import com.michaelrmossman.aucklandmuseum3api.util.valuesByIdentifier

@Composable
fun ObjectMultiText(
    identifier: String,
    result: OpacObject,
    title: String,
    modifier: Modifier = Modifier
) {
    val objectValues = result.valuesByIdentifier(
        identifier = identifier
    )

    objectValues?.let { values ->
        if (
            values.any { value ->
                value.isNotBlank()
            }
        ) {
            Text(
                text = title.fromHtml()
            )

            values.forEachIndexed { index, value ->

                if (value.isNotBlank()) {
                    Text(
                        text = value.trim().capitalise(),
                        modifier = modifier
                    )
                    HorizontalDivider(
                        thickness = when (
                            index == values.lastIndex
                        ) {
                            true -> dimensionResource(
                                R.dimen.details_last_divider_thickness
                            )
                            /* Material3 default is 1.0.dp */
                            else -> DividerDefaults.Thickness
                        }
                    )
                }
            }
        }
    }
}