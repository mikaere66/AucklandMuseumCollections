package com.michaelrmossman.aucklandmuseum3api.ui.persons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.Dp
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacPerson
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObject
import com.michaelrmossman.aucklandmuseum3api.ui.components.ItemImage
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_IMAGE_DERIVATIVE_MED
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_DOUBLE_PIPE
import com.michaelrmossman.aucklandmuseum3api.util.SEPARATOR_SEMI_COLON
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.getTextFromString
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml
import com.michaelrmossman.aucklandmuseum3api.util.imageTitleText

/* Shows all MEDIUM images related to a person */
@Composable
fun PersonImages(
    onClickImage: (Int, List<ImageObject>) -> Unit,
    result: OpacPerson,
    modifier: Modifier = Modifier
) {
    val imageObjects = mutableListOf<ImageObject>()
    val relatedTitles = mutableListOf<String>()

    val relationships =
        result.relationshipsCollection.relationships.filter { rel ->
            rel.relatedRecords.isNotEmpty()
        }
    relationships.forEach { relationship ->
        relationship.relatedRecords.forEach { related ->
            related.image.imageDerivatives.forEach { derivative ->
                if (
                    derivative.identifier
                    ==
                    IDENTIFIER_IMAGE_DERIVATIVE_MED
                ) {
                    imageObjects.add(
                        ImageObject(
                            imageId = related.image.imageId,
                            identifier = derivative.identifier,
                            url = derivative.url,
                            width = derivative.width,
                            height = derivative.height
                        )
                    )
                    relatedTitles.add(related.title)
                }
            }
        }
    }

    Text(
        text = pluralStringResource(
            R.plurals.details_related_images,
            imageObjects.size,
            imageObjects.size
        ).fromHtml()
    )

    if (imageObjects.size == relatedTitles.size) {
    imageObjects.forEachIndexed { index, image ->
            val titleText = relatedTitles[index].imageTitleText()
            ItemImage(
                modifier = modifier,
                onClickImage = {
                    onClickImage(
                        index,
//                        imageObjects.map { image ->
//                            image.second
//                        }
                        imageObjects
                    )
                },
                titleText = titleText,
                url = image.url
            )
        }
    }
}