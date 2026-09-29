package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SubscriptionTier {
    NONE,
    TRIAL,
    PRO
}

data class SubscriptionInfo(
    val tier: SubscriptionTier = SubscriptionTier.NONE,
    val trialStartedAt: Long = 0L,
    val isPro: Boolean = false,
    val activatedKey: String? = null,
    val activatedAt: Long = 0L,
    val userEmail: String = ""
) {
    companion object {
        // 3 days in milliseconds = 3 * 24 * 60 * 60 * 1000 = 259,200,000 ms
        const val TRIAL_DURATION_MS: Long = 3L * 24L * 60L * 60L * 1000L
    }

    val isTrialActive: Boolean
        get() {
            if (isPro) return false
            if (tier != SubscriptionTier.TRIAL || trialStartedAt <= 0L) return false
            val elapsed = System.currentTimeMillis() - trialStartedAt
            return elapsed < TRIAL_DURATION_MS
        }

    val isTrialExpired: Boolean
        get() {
            if (isPro) return false
            if (tier == SubscriptionTier.TRIAL && trialStartedAt > 0L) {
                val elapsed = System.currentTimeMillis() - trialStartedAt
                return elapsed >= TRIAL_DURATION_MS
            }
            return false
        }

    val trialRemainingMillis: Long
        get() {
            if (!isTrialActive) return 0L
            val elapsed = System.currentTimeMillis() - trialStartedAt
            return (TRIAL_DURATION_MS - elapsed).coerceAtLeast(0L)
        }

    val trialRemainingHours: Long
        get() = (trialRemainingMillis / (1000L * 60L * 60L)).coerceAtLeast(0L)

    val trialRemainingDays: Long
        get() = ((trialRemainingHours + 23L) / 24L).coerceAtLeast(0L)

    val trialRemainingFormatted: String
        get() {
            if (isPro) return "Pro Lifetime"
            if (isTrialExpired) return "Trial Expired"
            if (!isTrialActive) return "No Active Plan"
            val hours = trialRemainingHours
            return if (hours >= 24) {
                val days = trialRemainingDays
                "$days day${if (days > 1) "s" else ""} left"
            } else {
                "${hours.coerceAtLeast(1)}h left"
            }
        }

    val expiryDateFormatted: String
        get() {
            if (trialStartedAt <= 0L) return "N/A"
            val expiryTime = trialStartedAt + TRIAL_DURATION_MS
            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
            return sdf.format(Date(expiryTime))
        }

    val userAccountId: String
        get() = if (userEmail.isNotBlank()) userEmail.trim().lowercase() else "nobitanobi7209@gmail.com"
}

sealed class ActivationResult {
    object Success : ActivationResult()
    data class Error(val message: String) : ActivationResult()
}
