package com.michaelrmossman.aucklandmuseum3api.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.enum.Collection
import com.michaelrmossman.aucklandmuseum3api.util.EMOJI_ERROR_DELAY
import com.michaelrmossman.aucklandmuseum3api.util.EmojiInputFilter.Companion.containsEmoji
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBoxWithButton(
    isEnabled: Boolean,
    onClickClearButton: () -> Unit,
    onClickSearchButton: (String) -> Unit,
    onTextChanged: (String) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier,
    onClickManageSearch: ((Collection?) -> Unit)? = null,
    currentCollection: Collection? = null
) {
    val commonPadding = dimensionResource(R.dimen.padding_small)
    val iconSizeLarge = dimensionResource(R.dimen.icon_size_large)
    val iconSmallPadding = dimensionResource(R.dimen.padding_small)
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = isError) {
        if (isError) {
            delay(EMOJI_ERROR_DELAY) // 2000ms
            isError = false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            colors = TextFieldDefaults.colors(),
            isError = isError,
            leadingIcon = {
                onClickManageSearch?.let { onClick ->
                    SearchBoxActionMenu(
                        modifier = Modifier.padding(
                            horizontal = iconSmallPadding
                        ),
                        onClickManageSearch = onClick,
                        currentCollection = currentCollection
                    )
                }
            },
            maxLines = 1,
            modifier = Modifier
                .padding(
                    top = commonPadding,
                    end = commonPadding,
                    start = commonPadding
                )
                .weight(1F),
            onValueChange = { value ->
                // searchQuery = value
                if (containsEmoji(
                    value,
                    0,
                    value.length
                )) {
                    isError = true
                }
                onTextChanged(value)
            },
            placeholder = {
                Text(text = stringResource(R.string.search_hint))
            },
            singleLine = true,
            shape = RoundedCornerShape(
                dimensionResource(R.dimen.card_corner_shape)
            ),
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .padding(horizontal = iconSmallPadding),
                    onClick = {
                        // searchQuery = String()
                        onClickClearButton()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = stringResource(
                            R.string.search_clear
                        )
                    )
                }
            },
            value = searchQuery
        )
        IconButton(
            enabled = (isEnabled && searchQuery.length > 1),
            onClick = {
                onClickSearchButton(searchQuery)
            },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor =
                    MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.padding(top = commonPadding)
        ) {
            Icon(
                modifier = Modifier.size(iconSizeLarge),
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(
                    R.string.search_desc
                )
            )
        }
    }
}