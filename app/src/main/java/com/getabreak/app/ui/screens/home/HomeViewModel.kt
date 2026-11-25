package com.getabreak.app.ui.screens.home

import android.app.Application
import android.content.Intent
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import com.getabreak.app.features.CommitmentPasswordFeature
import com.getabreak.app.features.EmergencyBrakeFeature
import com.getabreak.app.system_integration.AccessibilityServiceController
import com.getabreak.app.system_integration.GetabreakAccessibilityService
import com.getabreak.app.system_integration.GetabreakDeviceAdminReceiver
import com.getabreak.app.system_integration.GetabreakState
import com.getabreak.app.util.NavigationUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * The state of the snackbar on the home screen.
 */
enum class HomeScreenSnackbarState {
    Hidden, ShowStartAcccessibilityServiceManually, CommitmentPasswordLocked, BrakeRunning
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(application) {
    /**
     * The current state of the accessibility service. This is a [StateFlow], so it can be observed
     * by other components.
     * @see GetabreakAccessibilityService.state
     */
    val getabreakState: StateFlow<GetabreakState> = GetabreakAccessibilityService.state

    private val _snackbarState = MutableStateFlow(HomeScreenSnackbarState.Hidden)
    val snackbarState: StateFlow<HomeScreenSnackbarState> = _snackbarState

    /**
     * Toggles the state of the accessibility service.
     * Returns null when blocked by the Commitment Password.
     * @param justAgreed whether the user agreed to the accessibility disclosure a moment ago. A
     * service getabreak cannot switch on by itself then opens the accessibility settings directly,
     * rather than asking once more in a snackbar.
     * @return the new state of the accessibility service or null if the (de-)activation failed
     * @see GetabreakAccessibilityService
     * @see GetabreakState
     */
    fun toggleGetabreakIsRunning(justAgreed: Boolean = false): GetabreakState? {
        Timber.d("state = ${getabreakState.value}")

        // Block stopping getabreak while Commitment Password is active and locked
        if (getabreakState.value == GetabreakState.Active
            && CommitmentPasswordFeature.isActivated
            && !CommitmentPasswordFeature.isSessionUnlocked()
        ) {
            setSnackbarState(HomeScreenSnackbarState.CommitmentPasswordLocked)
            return null
        }

        // stopping getabreak would end a brake on the spot
        if (getabreakState.value != GetabreakState.Inactive && EmergencyBrakeFeature.isBraking()) {
            setSnackbarState(HomeScreenSnackbarState.BrakeRunning)
            return null
        }

        val shouldBeRunning = getabreakState.value != GetabreakState.Active
        kotlin.runCatching {
            if (shouldBeRunning && AccessibilityServiceController.activate(application)) {
                return GetabreakState.Active
            } else if (!shouldBeRunning && AccessibilityServiceController.deactivate(application)) {
                return GetabreakState.Inactive
            }
        }

        if (shouldBeRunning && justAgreed) {
            NavigationUtil.openAccessibilitySettings(application)
            return null
        }
        setSnackbarState(HomeScreenSnackbarState.ShowStartAcccessibilityServiceManually)
        return null
    }

    fun setSnackbarState(state: HomeScreenSnackbarState) {
        _snackbarState.value = state
    }

    /**
     * Stops getabreak and all running features, revokes the device admin permission and uninstalls.
     * Blocked whenever Commitment Password is active.
     */
    fun uninstallGetabreak(): Boolean {
        if (CommitmentPasswordFeature.isActivated) {
            setSnackbarState(HomeScreenSnackbarState.CommitmentPasswordLocked)
            return false
        }
        if (EmergencyBrakeFeature.isBraking()) {
            setSnackbarState(HomeScreenSnackbarState.BrakeRunning)
            return false
        }
        GetabreakAccessibilityService.instance?.onDestroy()
        kotlin.runCatching { AccessibilityServiceController.deactivate(application) }
        kotlin.runCatching { GetabreakDeviceAdminReceiver.revokePermission(application) }
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = "package:${application.packageName}".toUri()
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        application.startActivity(intent)
        return true
    }
}