package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.michaelrmossman.aucklandmuseum3api.R

@Composable
fun ButtonWithIcon(
    @DrawableRes drawableId: Int,
    onClickButton: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier,
    buttonWidth: Dp = Dp.Unspecified,
    isEnabled: Boolean = true
) {

    Button(
        enabled = isEnabled,
        onClick = onClickButton,
        modifier = Modifier
            .widthIn(
                min = buttonWidth, max = buttonWidth
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(drawableId),
                modifier = modifier.padding(
                    horizontal = dimensionResource(
                        R.dimen.padding_medium
                    )
                ),
                contentDescription = null
            )

            Spacer(modifier = Modifier.weight(0.2F))
            Text(text = stringResource(stringId))
            Spacer(modifier = Modifier.weight(0.8F))
        }
    }
}