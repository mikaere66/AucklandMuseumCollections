package com.michaelrmossman.aucklandmuseum3api.util

import androidx.annotation.DimenRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.sp
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication.Companion.instance
import com.michaelrmossman.aucklandmuseum3api.R

/**
 * Text utility functions used throughout the app
 */
object TextUtils {

    @Composable
    @ReadOnlyComposable
    fun fontDimensionResource(@DimenRes id: Int) =
        dimensionResource(id = id).value.sp

    @Composable
    fun getAppSubtitle(
        listSize: Int,
        resultCount: Int,
        @StringRes stringId: Int
    ) : String {
        /* formatWithComma() returns a string */
        val downloaded = when (listSize) {
            0 -> 0
            else -> listSize.formatWithComma()
        }
        val total = when (resultCount) {
            0 -> 0
            else -> resultCount.formatWithComma()
        }
        return stringResource(
            stringId,
            downloaded,
            total
        )
    }

    @Composable
    fun getTextFromList(
        list: List<String>?,
        @PluralsRes pluralsId: Int,
        capitalise: Boolean = false,
        pipeSeparator: Boolean = false
    ) : AnnotatedString {
        val undefined = stringResource(R.string.text_undefined)
        val listFlattened = buildString {
            list?.forEachIndexed { index, item ->
                append(
                    when (capitalise) {
                        true -> item.capitalise()
                        else -> item
                    }
                )
                if (index < list.lastIndex) {
                    append(
                        when (pipeSeparator) {
                            true -> SEPARATOR_PIPE
                            else -> SEPARATOR_ITEM
                        }
                    )
                }
            }
        }
        return pluralStringResource(
            pluralsId,
            /* plural "zero" doesn't work */
            when (list?.isNotEmpty()) {
                true -> list.size
                else -> 1
            },
            when (list?.size) {
                0 -> undefined
                else -> listFlattened
            }
        ).fromHtml()
    }

    @Composable
    fun getTextFromString(
        @StringRes stringId: Int,
        string: String?,
        capitalise: Boolean = false
    ) : AnnotatedString {
        val undefined = stringResource(R.string.text_undefined)
        return stringResource(
            stringId,
            when (string?.isNotBlank()) {
                true -> when (capitalise) {
                    true -> string.capitalise()
                    else -> string
                }
                else -> undefined
            }
        ).fromHtml()
    }
}