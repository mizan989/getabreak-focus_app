package com.flx_apps.digitaldetox.ui.screens.premium

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.flx_apps.digitaldetox.R
import com.flx_apps.digitaldetox.premium.PremiumManager
import com.flx_apps.digitaldetox.premium.PremiumSheetController
import com.flx_apps.digitaldetox.premium.PremiumSheetTrigger
import com.flx_apps.digitaldetox.premium.PremiumSupport
import com.flx_apps.digitaldetox.review.AppReviewController
import com.flx_apps.digitaldetox.util.formatDurationMsShort

/**
 * App-global host for the wellbeing features information sheet.
 * In Break, all digital wellbeing features are completely free and unconstrained.
 * All donation and tip-jar actions have been removed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumSheetHost(viewModel: PremiumSheetViewModel = viewModel()) {
    val trigger by PremiumSheetController.trigger.collectAsState()
    val currentTrigger = trigger
    val activity = LocalActivity.current
    if (currentTrigger != null) {
        val isUnlocked by PremiumManager.isPremiumUnlocked.collectAsState()
        val impactStats by viewModel.impactStats.collectAsState()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) {
            viewModel.loadImpactStats()
            PremiumSupport.refreshEntitlement()
        }
        ModalBottomSheet(
            onDismissRequest = { PremiumSheetController.onDismiss() },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            Crossfade(targetState = isUnlocked, label = "premiumSheetState") { unlocked ->
                if (unlocked) {
                    PremiumUnlockedContent(
                        onReset = { PremiumManager.relock() },
                        onDone = {
                            PremiumSheetController.hide()
                            activity?.let { AppReviewController.maybeAskForReview(it) }
                        },
                    )
                } else {
                    PremiumLockedContent(
                        trigger = currentTrigger,
                        impactStats = impactStats,
                        onUnlock = { PremiumManager.unlock() },
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumLockedContent(
    trigger: PremiumSheetTrigger,
    impactStats: PremiumImpactStats?,
    onUnlock: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PremiumHeroIcon(icon = Icons.Default.WorkspacePremium)
        Spacer(Modifier.height(16.dp))
        Text(
            text = titleForTrigger(trigger),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.premium_sheet_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (impactStats != null && !impactStats.isEmpty) {
            Spacer(Modifier.height(16.dp))
            ImpactCard(impactStats)
        }

        Spacer(Modifier.height(16.dp))
        ExtrasCard()

        Spacer(Modifier.height(20.dp))
        PremiumLockedSheetActions(onUnlock = onUnlock)
    }
}

@Composable
private fun PremiumUnlockedContent(
    onReset: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PremiumHeroIcon(icon = Icons.Default.Check)
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.premium_unlocked_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.premium_unlocked_subtitle),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.premium_unlocked_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_done))
        }
        if (PremiumSupport.allowsFreeUnlock) {
            TextButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.premium_action_relock))
            }
        }
    }
}

@Composable
private fun titleForTrigger(trigger: PremiumSheetTrigger): String = when (trigger) {
    PremiumSheetTrigger.Generic -> stringResource(R.string.premium_sheet_title_generic)
    PremiumSheetTrigger.PowerUseNudge -> stringResource(R.string.premium_sheet_title_nudge)
    is PremiumSheetTrigger.LockedFeature -> stringResource(
        R.string.premium_sheet_title_locked, stringResource(trigger.featureLabelRes)
    )
}

@Composable
private fun PremiumHeroIcon(icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHighest,
                CircleShape,
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(32.dp),
        )
    }
}

@Composable
private fun ImpactCard(stats: PremiumImpactStats) {
    val context = LocalContext.current
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.premium_impact_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                if (stats.interventionCount > 0) {
                    ImpactTile(
                        value = stringResource(
                            R.string.premium_impact_count, stats.interventionCount
                        ),
                        label = stringResource(R.string.premium_impact_interventions),
                        modifier = Modifier.weight(1f),
                    )
                }
                if (stats.grayscaleTimeMs > 0) {
                    ImpactTile(
                        value = formatDurationMsShort(context, stats.grayscaleTimeMs),
                        label = stringResource(R.string.premium_impact_grayscale),
                        modifier = Modifier.weight(1f),
                    )
                }
                if (stats.daysTracked > 0) {
                    ImpactTile(
                        value = stats.daysTracked.toString(),
                        label = stringResource(R.string.premium_impact_days),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ImpactTile(value: String, label: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ExtrasCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.WorkspacePremium,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = stringResource(R.string.premium_extras_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = stringResource(R.string.premium_extras_body),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
