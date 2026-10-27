package com.flx_apps.digitaldetox.ui.screens.premium

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.flx_apps.digitaldetox.R

/**
 * FOSS bottom section of the premium sheet.
 * In Break, digital wellbeing features are 100% free and accessible to all users.
 * All donation and tip-jar actions have been removed.
 */
@Composable
fun PremiumLockedSheetActions(onUnlock: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onUnlock,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.premium_action_freeUnlock))
        }
        Text(
            text = stringResource(R.string.premium_locked_footer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
