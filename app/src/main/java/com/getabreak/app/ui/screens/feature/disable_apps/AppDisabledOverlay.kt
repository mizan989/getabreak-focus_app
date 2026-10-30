package com.getabreak.app.ui.screens.feature.disable_apps

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.getabreak.app.R
import com.getabreak.app.system_integration.OverlayContent
import com.getabreak.app.system_integration.OverlayService
import com.getabreak.app.ui.widgets.InterventionPrimaryButton
import com.getabreak.app.ui.widgets.InterventionScreen

class AppDisabledOverlayService : OverlayService(OverlayContent { AppDisabledOverlay() })

/**
 * The overlay that is displayed when the user tries to open a disabled app or website. Closing it
 * leads home from an app and back in the browser from a website, see [OverlayService.leave].
 */
@Preview
@Composable
fun AppDisabledOverlay() {
    val service = LocalContext.current as? OverlayService
    val overWebsite = service?.isOverWebsite == true
    InterventionScreen(
        icon = painterResource(id = R.drawable.ic_disable_app),
        title = stringResource(id = R.string.feature_disableApps_overlay_title),
        message = stringResource(
            id = if (overWebsite) {
                R.string.feature_disableApps_overlay_message_website
            } else {
                R.string.feature_disableApps_overlay_message
            }
        ) + " " + stringResource(id = R.string.feature_disableApps_overlay_message2),
    ) {
        InterventionPrimaryButton(
            text = stringResource(
                id = if (overWebsite) R.string.feature_disableApps_overlay_goBack else R.string.action_close
            ),
            onClick = { service?.leave() }
        )
    }
}
