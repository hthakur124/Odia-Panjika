package com.example.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Manages Google Mobile Ads (AdMob) initialization and Google UMP user consent
 * in compliance with Google Play Developer Program & EU/UK privacy policies.
 */
object AdMobManager {

    /**
     * Google's official Anchored Adaptive Banner test Ad Unit ID.
     * Replace with your live AdMob Banner Ad Unit ID before Play Store release.
     */
    const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741"

    private val isMobileAdsInitialized = AtomicBoolean(false)
    private val backgroundScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _canRequestAds = MutableStateFlow(false)
    val canRequestAds: StateFlow<Boolean> = _canRequestAds.asStateFlow()

    private val _isPrivacyOptionsRequired = MutableStateFlow(false)
    val isPrivacyOptionsRequired: StateFlow<Boolean> = _isPrivacyOptionsRequired.asStateFlow()

    fun gatherConsentAndInitialize(activity: Activity) {
        runCatching {
            val params = ConsentRequestParameters.Builder()
                .setTagForUnderAgeOfConsent(false)
                .build()

            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
            consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) {
                        updateConsentState(consentInformation)
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk(activity.applicationContext)
                        }
                    }
                },
                {
                    // Even if consent refresh fails (e.g., offline), check cached consent state
                    updateConsentState(consentInformation)
                    if (consentInformation.canRequestAds()) {
                        initializeMobileAdsSdk(activity.applicationContext)
                    }
                }
            )

            // If consent was already obtained in an earlier session, initialize immediately
            updateConsentState(consentInformation)
            if (consentInformation.canRequestAds()) {
                initializeMobileAdsSdk(activity.applicationContext)
            }
        }
    }

    fun showPrivacyOptionsForm(activity: Activity, onDismissed: () -> Unit = {}) {
        runCatching {
            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
            UserMessagingPlatform.showPrivacyOptionsForm(activity) {
                updateConsentState(consentInformation)
                onDismissed()
            }
        }.onFailure {
            onDismissed()
        }
    }

    private fun updateConsentState(consentInformation: ConsentInformation) {
        _canRequestAds.value = consentInformation.canRequestAds()
        _isPrivacyOptionsRequired.value =
            consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }

    private fun initializeMobileAdsSdk(appContext: Context) {
        if (isMobileAdsInitialized.getAndSet(true)) return
        backgroundScope.launch {
            runCatching {
                MobileAds.initialize(appContext)
            }
        }
    }
}
