package com.michaelrmossman.aucklandmuseum3api.ui.objects

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.model.OpacObject
import com.michaelrmossman.aucklandmuseum3api.objects.ImageObject
import com.michaelrmossman.aucklandmuseum3api.ui.components.ItemImage
import com.michaelrmossman.aucklandmuseum3api.util.IDENTIFIER_IMAGE_DERIVATIVE_MED
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml
import com.michaelrmossman.aucklandmuseum3api.util.imageTitleText

@Composable
fun ObjectImages(
    onClickImage: (Int, List<ImageObject>) -> Unit,
    result: OpacObject,
    modifier: Modifier = Modifier
) {
    val imageObjects = mutableListOf<ImageObject>()

    result.imagesCollection.images.forEach { image ->
        image.imageDerivatives.forEach { derivative ->
            if (
                derivative.identifier
                ==
                IDENTIFIER_IMAGE_DERIVATIVE_MED
            ) {
                if (derivative.url.isNotBlank()) {
                    imageObjects.add(
                        ImageObject(
                            image.imageId,
                            derivative.identifier,
                            derivative.url,
                            derivative.width,
                            derivative.height
                        )
                    )
                }
            }
        }
    }

    Text(
        text = pluralStringResource(
            R.plurals.details_object_images,
            imageObjects.size,
            imageObjects.size
        ).fromHtml()
    )

    imageObjects.forEachIndexed { index, image ->
        ItemImage(
            modifier = modifier,
            onClickImage = {
                onClickImage(
                    index,
                    imageObjects
                )
            },
            url = image.url
        )
    }
}