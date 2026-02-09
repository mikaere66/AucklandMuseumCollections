package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R

@Composable
fun MessageWithIcon(
    @DrawableRes drawableId: Int,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier
) {
    val iconPadding = dimensionResource(
        R.dimen.padding_mini
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(stringId))
        Icon(
            painter = painterResource(drawableId),
            modifier = modifier.padding(
                bottom = iconPadding,
                top = iconPadding,
                start = iconPadding
            ),
            contentDescription = null
        )
    }
}