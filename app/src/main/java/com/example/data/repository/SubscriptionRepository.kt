package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.ActivationResult
import com.example.data.model.SubscriptionInfo
import com.example.data.model.SubscriptionTier
import com.example.data.security.ActivationKeySecurity

class SubscriptionRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("preptrack_subscriptions", Context.MODE_PRIVATE)

    private fun tierKey(email: String) = "tier_${ActivationKeySecurity.normalizeEmail(email)}"
    private fun trialStartKey(email: String) = "trial_start_${ActivationKeySecurity.normalizeEmail(email)}"
    private fun proKey(email: String) = "pro_key_${ActivationKeySecurity.normalizeEmail(email)}"
    private fun proDateKey(email: String) = "pro_date_${ActivationKeySecurity.normalizeEmail(email)}"

    fun getSubscription(email: String): SubscriptionInfo {
        val cleanEmail = ActivationKeySecurity.normalizeEmail(email)
        val isPro = prefs.getBoolean("is_pro_$cleanEmail", false)
        val tierString = prefs.getString(tierKey(cleanEmail), SubscriptionTier.NONE.name) ?: SubscriptionTier.NONE.name
        val tier = try {
            SubscriptionTier.valueOf(tierString)
        } catch (_: Exception) {
            SubscriptionTier.NONE
        }
        val trialStart = prefs.getLong(trialStartKey(cleanEmail), 0L)
        val activatedKey = prefs.getString(proKey(cleanEmail), null)
        val activatedAt = prefs.getLong(proDateKey(cleanEmail), 0L)

        return SubscriptionInfo(
            tier = if (isPro) SubscriptionTier.PRO else tier,
            trialStartedAt = trialStart,
            isPro = isPro,
            activatedKey = activatedKey,
            activatedAt = activatedAt,
            userEmail = cleanEmail
        )
    }

    fun start3DayTrial(email: String): SubscriptionInfo {
        val cleanEmail = ActivationKeySecurity.normalizeEmail(email)
        val existing = getSubscription(cleanEmail)
        if (existing.isPro) return existing

        // Only start if not already started; or refresh timestamp if NONE
        val startTime = if (existing.trialStartedAt > 0L) existing.trialStartedAt else System.currentTimeMillis()

        prefs.edit()
            .putString(tierKey(cleanEmail), SubscriptionTier.TRIAL.name)
            .putLong(trialStartKey(cleanEmail), startTime)
            .apply()

        return getSubscription(cleanEmail)
    }

    fun activateProWithKey(email: String, enteredKey: String): ActivationResult {
        val cleanEmail = ActivationKeySecurity.normalizeEmail(email)
        val cleanKey = ActivationKeySecurity.cleanKeyFormat(enteredKey)

        if (cleanKey.isBlank()) {
            return ActivationResult.Error("Please enter an activation key")
        }

        val isValid = ActivationKeySecurity.validateKey(cleanKey, cleanEmail)
        if (!isValid) {
            return ActivationResult.Error(
                "Invalid activation key.\nPlease check your key and make sure it was entered correctly."
            )
        }

        // Check if this key was already used and bound to another account
        val boundAccountKey = "bound_email_$cleanKey"
        val existingBoundEmail = prefs.getString(boundAccountKey, null)
        if (existingBoundEmail != null && !existingBoundEmail.equals(cleanEmail, ignoreCase = true)) {
            return ActivationResult.Error(
                "This key has already been activated and bound to another account ID ($existingBoundEmail).\nEach key can only be activated for a single account and cannot be reused."
            )
        }

        // Successfully verified! Save Pro status permanently and bind key
        val now = System.currentTimeMillis()
        prefs.edit()
            .putBoolean("is_pro_$cleanEmail", true)
            .putString(tierKey(cleanEmail), SubscriptionTier.PRO.name)
            .putString(proKey(cleanEmail), cleanKey)
            .putLong(proDateKey(cleanEmail), now)
            .putString(boundAccountKey, cleanEmail)
            .apply()

        return ActivationResult.Success
    }

    /**
     * Testing helpers: allows testing the 3-day expired state or resetting trial
     */
    fun expireTrialForTesting(email: String): SubscriptionInfo {
        val cleanEmail = ActivationKeySecurity.normalizeEmail(email)
        // Set trial start to 4 days ago
        val fourDaysAgo = System.currentTimeMillis() - (4L * 24L * 60L * 60L * 1000L)
        prefs.edit()
            .putBoolean("is_pro_$cleanEmail", false)
            .putString(tierKey(cleanEmail), SubscriptionTier.TRIAL.name)
            .putLong(trialStartKey(cleanEmail), fourDaysAgo)
            .remove(proKey(cleanEmail))
            .apply()

        return getSubscription(cleanEmail)
    }

    fun resetTrialForTesting(email: String): SubscriptionInfo {
        val cleanEmail = ActivationKeySecurity.normalizeEmail(email)
        prefs.edit()
            .remove("is_pro_$cleanEmail")
            .remove(tierKey(cleanEmail))
            .remove(trialStartKey(cleanEmail))
            .remove(proKey(cleanEmail))
            .remove(proDateKey(cleanEmail))
            .apply()

        return getSubscription(cleanEmail)
    }
}
