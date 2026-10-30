package com.getabreak.app.premium

/**
 * FOSS (GitHub / F-Droid) implementation of [PremiumSupportProvider].
 * All digital wellbeing features in getabreak are completely free and unlocked.
 * Donation and tip-jar links have been removed.
 */
object PremiumSupport : PremiumSupportProvider {
    override val supportsInAppPurchase = false
    override val allowsFreeUnlock = true
    override val supportLinks: List<SupportLink> = emptyList()
    override val bitcoinAddress: String? = null
}
