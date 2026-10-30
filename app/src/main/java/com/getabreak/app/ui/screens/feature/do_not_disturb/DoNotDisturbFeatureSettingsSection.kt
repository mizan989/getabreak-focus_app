package com.getabreak.app.ui.screens.feature.do_not_disturb

import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getabreak.app.R
import com.getabreak.app.ui.screens.feature.OpenScheduleTile
import com.getabreak.app.ui.widgets.SettingsGroup
import com.getabreak.app.ui.widgets.SimpleListTile

@Composable
fun DoNotDisturbFeatureSettingsSection(
    viewModel: DoNotDisturbFeatureSettingsViewModel = viewModel()
) {
    SettingsGroup {
        OpenScheduleTile()
        SimpleListTile(
            titleText = stringResource(id = R.string.feature_doNotDisturb_systemSettings),
            subtitleText = stringResource(
                id = R.string.feature_doNotDisturb_systemSettings_description
            ),
            trailing = {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            },
            onClick = { viewModel.openDoNotDisturbSystemSettings() },
            leadingIcon = Icons.Default.Settings
        )
    }
}