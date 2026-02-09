package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.util.TextUtils.fontDimensionResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoLineAppBar(
    actions: @Composable RowScope.() -> Unit,
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    val subtitleFontSize = fontDimensionResource(R.dimen.font_size_subtitle)

    TopAppBar(
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer,
            titleContentColor =
                MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (stringId != 0) { /* Same as above: NavigationIcon not used */
                BackButton(onClickBackButton = onClickBackButton)
            }
        },
        title = {
            Column {
                /* Title not used if TopAppBar is @ centre of large screen */
                if (stringId != 0) {
                    Text(
                        text = stringResource(stringId),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                subtitle?.let { sub ->
                    Text(
                        fontSize = subtitleFontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = sub
                    )
                }
            }
        }
    )
}