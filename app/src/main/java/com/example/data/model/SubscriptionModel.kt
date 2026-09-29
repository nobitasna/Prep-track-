package com.example.data.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class SubscriptionTier {
    NONE,
    TRIAL,
    PRO
}

enum class ProType {
    NONE,
    MONTHLY, // 1 Month (30 Days)
    LIFETIME // Permanent
}

data class SubscriptionInfo(
    val tier: SubscriptionTier = SubscriptionTier.NONE,
    val proType: ProType = ProType.NONE,
    val trialStartedAt: Long = 0L,
    val isPro: Boolean = false,
    val activatedKey: String? = null,
    val activatedAt: Long = 0L,
    val userEmail: String = ""
) {
    companion object {
        // 3 days in milliseconds = 3 * 24 * 60 * 60 * 1000 = 259,200,000 ms
        const val TRIAL_DURATION_MS: Long = 3L * 24L * 60L * 60L * 1000L
        // 30 days in milliseconds = 30 * 24 * 60 * 60 * 1000 = 2,592,000,000 ms
        const val MONTHLY_DURATION_MS: Long = 30L * 24L * 60L * 60L * 1000L
    }

    val isLifetimePro: Boolean
        get() = isPro && (proType == ProType.LIFETIME || proType == ProType.NONE)

    val isMonthlyPro: Boolean
        get() = isPro && proType == ProType.MONTHLY

    val isMonthlyExpired: Boolean
        get() {
            if (!isMonthlyPro || activatedAt <= 0L) return false
            val elapsed = System.currentTimeMillis() - activatedAt
            return elapsed >= MONTHLY_DURATION_MS
        }

    val isProActive: Boolean
        get() {
            if (!isPro) return false
            if (isLifetimePro) return true
            if (isMonthlyPro) return !isMonthlyExpired
            return true
        }

    val monthlyRemainingMillis: Long
        get() {
            if (!isMonthlyPro || isMonthlyExpired) return 0L
            val elapsed = System.currentTimeMillis() - activatedAt
            return (MONTHLY_DURATION_MS - elapsed).coerceAtLeast(0L)
        }

    val monthlyRemainingDays: Long
        get() = ((monthlyRemainingMillis / (1000L * 60L * 60L * 24L)) + 1).coerceAtLeast(1L)

    val isTrialActive: Boolean
        get() {
            if (isProActive) return false
            if (tier != SubscriptionTier.TRIAL || trialStartedAt <= 0L) return false
            val elapsed = System.currentTimeMillis() - trialStartedAt
            return elapsed < TRIAL_DURATION_MS
        }

    val isTrialExpired: Boolean
        get() {
            if (isProActive) return false
            if (tier == SubscriptionTier.TRIAL && trialStartedAt > 0L) {
                val elapsed = System.currentTimeMillis() - trialStartedAt
                return elapsed >= TRIAL_DURATION_MS
            }
            return false
        }

    val isBlocked: Boolean
        get() = isTrialExpired || (isMonthlyPro && isMonthlyExpired)

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
            if (isLifetimePro) return "Pro Lifetime"
            if (isMonthlyPro && !isMonthlyExpired) return "Pro ${monthlyRemainingDays}d left"
            if (isMonthlyExpired) return "Monthly Expired"
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
            val baseTime = if (isMonthlyPro) activatedAt else trialStartedAt
            val duration = if (isMonthlyPro) MONTHLY_DURATION_MS else TRIAL_DURATION_MS
            if (baseTime <= 0L) return "N/A"
            val expiryTime = baseTime + duration
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
