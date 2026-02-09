package com.michaelrmossman.aucklandmuseum3api.ui.settings

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.michaelrmossman.aucklandmuseum3api.R
import com.michaelrmossman.aucklandmuseum3api.ui.components.DynamicActionMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.LargeDropdownMenu
import com.michaelrmossman.aucklandmuseum3api.ui.components.TwoLineAppBar
import com.michaelrmossman.aucklandmuseum3api.util.DialogUtils.ConfirmResetSettingsDialog
import com.michaelrmossman.aucklandmuseum3api.util.fromHtml

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onClickBackButton: () -> Unit,
    @StringRes stringId: Int
) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.Factory
    )
    val resultsSize by settingsViewModel.resultsSize.observeAsState()
    val qtyHistory by settingsViewModel.qtyHistory.observeAsState()
    val qtyHistoryExists by remember {
        derivedStateOf { (qtyHistory != null && qtyHistory != 0) }
    }
    val saveFaveDate by settingsViewModel.saveFaveDate.observeAsState()
    val saveHistory by settingsViewModel.saveHistory.observeAsState()
    val scrollState = rememberScrollState()
    var showResetDialog by remember { mutableStateOf(false) }
    val sortOrder by settingsViewModel.sortOrder.observeAsState()
    val viewState by settingsViewModel.settingsUiState.collectAsState()
    // val zoomFullImage by settingsViewModel.zoomFullImage.observeAsState()

    val settingsNotDefault by remember {
        derivedStateOf {
            (resultsSize != null && resultsSize != 0)
            ||
            (saveFaveDate != null && saveFaveDate != 0)
            ||
            (saveHistory != null && saveHistory != 0)
            ||
            (sortOrder != null && sortOrder != 0)
            // || (zoomFullImage != null && zoomFullImage != 0)
        }
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = viewState.historyItemsDeleted) {
        /* Watch for a flag indicating that history items
           were cleared ... when present, show message */
        if (viewState.historyItemsDeleted != 0) {
            settingsViewModel.resetHistoryState()
            Toast.makeText(
                context,
                R.string.settings_toast_done,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TwoLineAppBar(
                actions = {
                    DynamicActionMenu(
                        isEnabled = listOf(
                            qtyHistoryExists,
                            settingsNotDefault
                        ),
                        menuLabels = listOf(
                            R.string.menu_clear_history,
                            R.string.menu_reset_settings
                        ).map { stringId ->
                            stringResource(stringId)
                        },
                        onClickActions = listOf(
                            { settingsViewModel.clearHistoryItems() },
                            { showResetDialog = true }
                        )
                    )
                },
                onClickBackButton = onClickBackButton,
                stringId = R.string.app_name,
                subtitle = stringResource(stringId)
            )
        }
    ) { contentPadding ->

        if (showResetDialog) {
            ConfirmResetSettingsDialog(
                onClickConfirm = {
                    showResetDialog = false
                    settingsViewModel.resetAllSettings()
                    onClickBackButton()
                },
                onClickDismiss = { showResetDialog = false }
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.spacing_vertical_small)
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = contentPadding.calculateBottomPadding(),
                    top = contentPadding.calculateTopPadding()
                )
                .verticalScroll(scrollState)
        ) {

            resultsSize?.let { size ->
                SettingMenu(
                    modifier = Modifier.padding(
                        end = dimensionResource(R.dimen.padding_micro),
                        start = dimensionResource(R.dimen.padding_micro),
                        top = dimensionResource(R.dimen.padding_small)
                    ),
                    settingLabelId = R.string.setting_num_results_label,
                    settingStringId = R.string.setting_num_results_text,
                    settingIndex = size,
                    settings = integerArrayResource(
                        R.array.settings_num_results
                    ).map { setting -> setting.toString() },
                    saveSetting = { menuIndex ->
                        settingsViewModel.setResultsSize(
                            index = menuIndex
                        )
                    }
                )
            }

            saveHistory?.let { save ->
                /* Note no HorizontalDivider */
                SettingSwitch(
                    onSwitch = { isChecked ->
                        settingsViewModel.setSaveHistory(
                            save = isChecked
                        )
                    },
                    settingStringId = R.string.setting_save_history_text,
                    switchedOn = save
                )
            }

            sortOrder?.let { order ->
                SettingDivider()

                SettingSwitch(
                    onSwitch = { isChecked ->
                        settingsViewModel.setSortOrder(
                            order = isChecked
                        )
                    },
                    settingStringId = R.string.setting_sort_by_descending,
                    switchedOn = order
                )
            }

            saveFaveDate?.let { saveDate ->
                SettingDivider()

                SettingSwitch(
                    onSwitch = { isChecked ->
                        settingsViewModel.setSaveFaveDate(
                            saveDate = isChecked
                        )
                    },
                    settingStringId = R.string.setting_save_date_faves,
                    switchedOn = saveDate
                )
            }

//            zoomFullImage?.let { zoomImage ->
//                SettingDivider()

//                SettingSwitch(
//                    onSwitch = { isChecked ->
//                        settingsViewModel.setZoomFullImage(
//                            zoomImage = isChecked
//                        )
//                    },
//                    settingStringId = R.string.setting_zoom_images,
//                    switchedOn = zoomImage
//                )
//            }
        }
    }
}

@Composable
fun SettingDivider(
    modifier: Modifier = Modifier
) {
    val dividerPadding = dimensionResource(R.dimen.padding_small)

    HorizontalDivider(
        modifier = modifier.padding(vertical = dividerPadding)
    )
}

@Composable
fun SettingMenu(
    @StringRes settingLabelId: Int,
    @StringRes settingStringId: Int,
    settingIndex: Int,
    settings: List<String>,
    saveSetting: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val horizontalPadding = dimensionResource(R.dimen.padding_small)
    var menuIndex by rememberSaveable {
        mutableIntStateOf(settingIndex)
    }

    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        text = stringResource(settingStringId)
    )
    LargeDropdownMenu(
        label = stringResource(settingLabelId),
        items = settings,
        selectedIndex = menuIndex,
        onItemSelected = { index, _ ->
            menuIndex = index
            saveSetting(index)
        },
        modifier = Modifier.padding(horizontal = horizontalPadding)
    )
}

@Composable
fun SettingSwitch(
    onSwitch: (Int) -> Unit,
    @StringRes settingStringId: Int,
    switchedOn: Int,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true
) {
    val rowHorizontalPadding = dimensionResource(R.dimen.padding_small)
    val textHorizontalPadding = dimensionResource(R.dimen.padding_mini)
    val verticalPadding = dimensionResource(R.dimen.padding_none)
    var switchItemSwitchedOn by rememberSaveable {
        /* Allow for random debug vals */
        mutableStateOf(switchedOn > 0)
    }

    Row(
        modifier = modifier.padding(
            end = rowHorizontalPadding,
            start = rowHorizontalPadding,
            top = verticalPadding
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = textHorizontalPadding)
                .weight(1F),
            text = stringResource(settingStringId).fromHtml()
        )
        Switch(
            checked = switchItemSwitchedOn,
            enabled = isEnabled,
            onCheckedChange = { isChecked ->
                switchItemSwitchedOn = isChecked
                onSwitch(
                    when (isChecked) {
                        true -> 1
                        else -> 0
                    }
                )
            },
            thumbContent = when (switchItemSwitchedOn) {
                false -> null
                else -> {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(
                                SwitchDefaults.IconSize
                            )
                        )
                    }
                }
            }
        )
    }
}