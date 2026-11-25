package com.getabreak.app.system_integration

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.getabreak.app.features.PauseButtonFeature

/**
 * The [PauseTileService] is the tile that is shown in the quick settings. It allows the user to
 * toggle the pause state of the [GetabreakAccessibilityService].
 */
class PauseTileService : TileService() {
    /**
     * Called when the user clicks the tile.
     * Will toggle the pause state of the [GetabreakAccessibilityService].
     */
    override fun onClick() {
        super.onClick()

        if (GetabreakAccessibilityService.instance == null) {
            return
        }

        PauseButtonFeature.togglePause(this)
        updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTile()
    }

    /**
     * Updates the tile to the current state of the [GetabreakAccessibilityService].
     * @see GetabreakAccessibilityService
     * @see GetabreakState
     */
    private fun updateTile() {
        val state = when (GetabreakAccessibilityService.updateState()) {
            GetabreakState.Inactive -> Tile.STATE_UNAVAILABLE
            GetabreakState.Paused -> Tile.STATE_ACTIVE
            GetabreakState.Active -> Tile.STATE_INACTIVE
        }

        // qsTile is null outside the listening window
        val tile = qsTile ?: return
        tile.state = state
        tile.updateTile()
    }
}