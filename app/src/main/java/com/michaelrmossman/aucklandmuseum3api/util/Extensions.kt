package com.michaelrmossman.aucklandmuseum3api.util

import android.graphics.Bitmap
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.MuseumApplication.Companion.instance
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.model.RelatedRecord
import com.michaelrmossman.aucklandmuseum3api.model.RelationshipsCollection
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromString
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

/**
 * Extension functions used throughout the app
 */

fun Bitmap.toByteArrayAndSizeInBytes(): Double {
    val stream = ByteArrayOutputStream()
    /* Compress the bitmap to PNG or JPEG format
       (PNG generally results in larger sizes) */
    this.compress(Bitmap.CompressFormat.PNG,100,stream)
    val byteArray = stream.toByteArray()
    // Convert bytes to kilobytes
    return byteArray.size.toDouble() // .div(1024.0)
}

fun Collection.humanReadable(): String {
    return this.toString().replace(
        "+"," "
    )
}

@Composable
fun Double.imageSizeFormatted(): String {
    return when (this) {
        0.0 -> stringResource(
            R.string.image_info_0_bytes,
            stringResource(R.string.text_undefined)
        )
        else -> {
            val stringId = when (this) {
                in 1.0..1023.0 -> {
                    R.string.image_info_bytes // 0 dec places
                }
                else -> R.string.image_info_x_bytes // 2 dec
            }
            stringResource(
                stringId,
                when (this) {
                    in 1.0..1023.0 -> this    // bytes
                    in 1024.0..1048575.0 -> {    // Kb
                        this.div(1024)
                    }
                    in 1048576.0..1073741823.0 -> {
                        this.div(1024).div(1024) // Mb
                    }
                    else -> this.div(            // Gb
                        1024
                    ).div(
                        1024
                    ).div(
                        1024
                    )
                },
                when (this) {
                    in 1.0..1023.0 -> {
                        "bytes" // e.g. Size: 123 bytes
                    }
                    in 1024.0..1048575.0 -> {
                        "Kb"      // e.g. Size: 1.23 Kb
                    }
                    in 1048576.0..1073741823.0 -> {
                        "Mb"      // e.g. Size: 1.23 Mb
                    }
                    // e.g. Size: 1.23 Gb, tho unlikely
                    else -> "Gb"
                }
            )
        }
    }
}

fun Int.formatWithComma(): String {
    return NumberFormat.getNumberInstance(
        Locale.getDefault()
    ).format(this)
}

fun List<Collection>.facets(): List<Pair<String, String>> {
    val facets = mutableListOf<Pair<String, String>>()
    this.forEach { collection ->
        facets.add(
            when (collection) {
                Collection.CollectionDocHeritage,
                Collection.CollectionHumanHistory,
                Collection.CollectionNatSciences -> {
                    /* e.g. collection_area:Documentary+Heritage */
                    Pair(
                        MUSEUM_FACET_COLLECTION,
                        collection.toString()
                    )
                }
                else -> Pair(
                    /* Would be a dept WITHIN a collection
                       e.g. department:Museum+archives */
                    MUSEUM_FACET_DEPARTMENT,
                    collection.toString()
                )
            }
        )
    }
    // android.util.Log.d("HEY",facets.size.toString())
    return facets
}

fun Long.parseMillisToKiwiDate(): String {
    return SimpleDateFormat(
        /* UK uses lowercase AM/PM */
        KIWI_UPDATE_FORMAT, Locale.UK
    ).format(Date(this))
}

// OBJECTS

@Composable
fun OpacObject.subtitleText(): List<String> {
    return this.valuesByIdentifier(
        IDENTIFIER_DEPARTMENT
    )?: listOf(stringResource(R.string.text_undefined))
}

@Composable
fun OpacObject.titleText(): String {
    val titles = this.valuesByIdentifier(IDENTIFIER_TYPE_TITLE)
    return titles?.joinToString(
        SEPARATOR_ITEM
    )?: stringResource(R.string.text_undefined)
}

fun OpacObject.valuesByIdentifier(
    identifier: String
) : List<String>? {
    val test = opacObjectFieldSets.singleOrNull { set ->
        set.identifier == identifier
    }?.opacObjectFields?.map { field ->
        field.value
    }?: listOf(instance.getString(R.string.text_undefined))
    return test
}

// PERSONS

@Composable
fun OpacPerson.subtitleText(): List<String> {
    return this.valuesByIdentifier(
        IDENTIFIER_PERSON_CORP_TYPE
    )?: listOf(stringResource(R.string.text_undefined))
}

@Composable
fun OpacPerson.titleText(): String {
    val titles = this.valuesByIdentifier(IDENTIFIER_NAME_FIRST_LAST)
    return titles?.joinToString(
        SEPARATOR_ITEM
    )?: stringResource(R.string.text_undefined)
}

fun OpacPerson.valuesByIdentifier(
    identifier: String,
    showIfBlank: Boolean = true
) : List<String>? {
    return opacPersonFieldSets.singleOrNull { set ->
        set.identifier == identifier
        &&
        set.opacPersonFields.any { field ->
            field.value.isNotBlank()
        }
    }?.opacPersonFields?.map { field ->
        field.value
    }?: listOf (when (showIfBlank) {
        true -> instance.getString(R.string.text_undefined)
        else -> String()
    })
}

// COMMON

fun RelationshipsCollection.relatedRecords(): List<Triple<String, String, String>> {
    /* BottomSheet will be disabled, if an empty list is returned */
    val relatedRecords = mutableListOf<Triple<String, String, String>>()

    val relationships = relationships.filter { rel ->
            rel.relatedRecords.isNotEmpty()
        }

    relationships.forEach { relationship ->

        relationship.relatedRecords.forEach { related ->

            relatedRecords.add(
                Triple(
                    relationship.relatedRecordType,
                    related.relatedRecordId,
                    related.title
                )
            )
        }
    }

    return relatedRecords
}

// OTHER

fun ResponseBody.errorMessage(): String {
    val jObjError = JSONObject(this.string())
    return try {
        when (jObjError.toString().contains("requestMessage")) {
            true -> jObjError.get("requestMessage").toString()
            else -> jObjError.get("message").toString()
        }

    } catch (ex: JSONException) {
        println(ex)
        String()
    }
}

fun String.capitalise(): String {
    return when (this.isBlank()) {
        true -> this
        else -> this.replaceFirstChar { string ->
            string.titlecase()
        }
    }
}

@Composable
fun String.fromHtml(): AnnotatedString {
    return AnnotatedString.Companion.fromHtml(
        htmlString = this,
        linkStyles = TextLinkStyles(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        )
    )
}

@Composable
fun String.imageTitleText(): AnnotatedString {
    val sep1 = SEPARATOR_DOUBLE_PIPE
    val sep2 = SEPARATOR_SEMI_COLON
    val title = when (this.contains(sep1)) {
        /* If string contains 1 or more instances of "||"
           title is everything AFTER the last instance */
        true -> this.substring(
            this.lastIndexOf(
                sep1
            ).plus(
                sep1.length
            )
        )
        /* Similar to above, but ";" (at least 2) */
        else -> when (this.contains(sep2)) {
            true -> when (this.count { char ->
                sep2.contains(char)
            }) {
                in -1..1 -> this
                else -> this.substring(
                    this.indexOf(sep2).plus(
                        sep2.length
                    ),
                    this.indexOf(
                        sep2,
                        this.indexOf(
                            sep2
                        ).plus(
                            sep2.length
                        )
                    )
                )
            }
            else -> this
        }
    }
    return getTextFromString(
        stringId = R.string.details_image_title,
        string = title,
        capitalise = true
    )
}

fun String.toSecureUrl(): String {
    return when (this.isBlank()) {
        true -> this
        else -> this.replaceFirst(
            "http:","https:"
        )
    }
}