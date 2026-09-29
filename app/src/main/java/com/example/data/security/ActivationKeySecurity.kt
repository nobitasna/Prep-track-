package com.example.data.security

import com.example.data.model.ProType
import java.security.MessageDigest
import java.util.Locale

object ActivationKeySecurity {
    // Secret internal salt ensuring cryptographic uniqueness
    private const val SECRET_MASTER_SALT = "PREP_TRACK_PRO_ACTIVATION_KEY_SECURE_SALT_2026_V1"

    /**
     * 25 Official Monthly (30-Day) Activation Keys.
     * Each key provides 1 month (30 days) of Pro access and automatically expires after 30 days.
     * Once activated, each key is locked to the user's specific Account ID.
     */
    val MONTHLY_MASTER_KEYS: List<String> = listOf(
        "PREP-MTH-4821-9B3C", // Monthly Key 01
        "PREP-MTH-7395-1E8A", // Monthly Key 02
        "PREP-MTH-2614-8D7F", // Monthly Key 03
        "PREP-MTH-9158-3C2E", // Monthly Key 04
        "PREP-MTH-5472-0A9D", // Monthly Key 05
        "PREP-MTH-3829-7F1B", // Monthly Key 06
        "PREP-MTH-6104-E52C", // Monthly Key 07
        "PREP-MTH-8273-4A6E", // Monthly Key 08
        "PREP-MTH-1936-D80F", // Monthly Key 09
        "PREP-MTH-7541-2B9A", // Monthly Key 10
        "PREP-MTH-4098-6C3D", // Monthly Key 11
        "PREP-MTH-8625-1F7E", // Monthly Key 12
        "PREP-MTH-3170-9D4A", // Monthly Key 13
        "PREP-MTH-9582-5E1C", // Monthly Key 14
        "PREP-MTH-2739-8A0B", // Monthly Key 15
        "PREP-MTH-6415-3C7F", // Monthly Key 16
        "PREP-MTH-1850-7B2D", // Monthly Key 17
        "PREP-MTH-5294-0E8A", // Monthly Key 18
        "PREP-MTH-8703-9F4C", // Monthly Key 19
        "PREP-MTH-3467-1A5E", // Monthly Key 20
        "PREP-MTH-7912-6D3B", // Monthly Key 21
        "PREP-MTH-4580-2C9F", // Monthly Key 22
        "PREP-MTH-9236-8E1A", // Monthly Key 23
        "PREP-MTH-1674-5B0D", // Monthly Key 24
        "PREP-MTH-6809-3F7E"  // Monthly Key 25
    )

    /**
     * 25 Official Lifetime Activation Keys.
     * Permanent unlimited access with no expiration.
     */
    val LIFETIME_MASTER_KEYS: List<String> = listOf(
        "PREP-PRO-9841-7A2F", // Lifetime Key 01
        "PREP-PRO-3194-E8BC", // Lifetime Key 02
        "PREP-PRO-5629-4D1A", // Lifetime Key 03
        "PREP-PRO-8713-C95E", // Lifetime Key 04
        "PREP-PRO-2408-B63F", // Lifetime Key 05
        "PREP-PRO-6952-1E7D", // Lifetime Key 06
        "PREP-PRO-4187-F30A", // Lifetime Key 07
        "PREP-PRO-7360-9B4C", // Lifetime Key 08
        "PREP-PRO-1594-A82D", // Lifetime Key 09
        "PREP-PRO-8246-3C7E", // Lifetime Key 10
        "PREP-PRO-3701-E59B", // Lifetime Key 11
        "PREP-PRO-6489-2A1F", // Lifetime Key 12
        "PREP-PRO-9135-D74C", // Lifetime Key 13
        "PREP-PRO-5820-F63E", // Lifetime Key 14
        "PREP-PRO-2974-8B1A", // Lifetime Key 15
        "PREP-PRO-7603-C49D", // Lifetime Key 16
        "PREP-PRO-4358-1E2B", // Lifetime Key 17
        "PREP-PRO-1892-A76F", // Lifetime Key 18
        "PREP-PRO-8527-3D0C", // Lifetime Key 19
        "PREP-PRO-6214-E98A", // Lifetime Key 20
        "PREP-PRO-3940-5F1B", // Lifetime Key 21
        "PREP-PRO-7186-B32E", // Lifetime Key 22
        "PREP-PRO-5409-8C7D", // Lifetime Key 23
        "PREP-PRO-2631-4A9E", // Lifetime Key 24
        "PREP-PRO-9758-F10C"  // Lifetime Key 25
    )

    /**
     * Determines whether an activation key is Monthly (30-day) or Lifetime.
     */
    fun getKeyType(key: String): ProType {
        val clean = cleanKeyFormat(key)
        return when {
            MONTHLY_MASTER_KEYS.any { it.equals(clean, ignoreCase = true) } -> ProType.MONTHLY
            clean.contains("-MTH-") -> ProType.MONTHLY
            else -> ProType.LIFETIME
        }
    }

    /**
     * Validates whether the entered activation key is valid.
     * Matches against either Monthly keys, Lifetime keys, or account-specific cryptographic key.
     */
    fun validateKey(enteredKey: String, email: String): Boolean {
        val normalizedEntered = cleanKeyFormat(enteredKey)
        if (MONTHLY_MASTER_KEYS.any { it.equals(normalizedEntered, ignoreCase = true) }) {
            return true
        }
        if (LIFETIME_MASTER_KEYS.any { it.equals(normalizedEntered, ignoreCase = true) }) {
            return true
        }
        val expected = generateKeyForUser(email)
        return normalizedEntered.equals(expected, ignoreCase = true)
    }

    /**
     * Generates a unique 12-character hexadecimal key grouped in 3 chunks:
     * Format: PREP-XXXX-XXXX-XXXX
     * Deterministically bound to the normalized account ID (email).
     */
    fun generateKeyForUser(email: String): String {
        val cleanId = normalizeEmail(email)
        val input = "$cleanId:$SECRET_MASTER_SALT"
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(input.toByteArray(Charsets.UTF_8))
        val hex = digest.joinToString("") { "%02X".format(it) }

        val p1 = hex.substring(0, 4)
        val p2 = hex.substring(4, 8)
        val p3 = hex.substring(8, 12)
        return "PREP-$p1-$p2-$p3"
    }

    /**
     * Normalizes and cleans the key string (e.g. trims whitespace, uppercase, fixes dashes)
     */
    fun cleanKeyFormat(key: String): String {
        val clean = key.trim().uppercase(Locale.ROOT)
        val stripped = clean.replace("-", "").replace(" ", "")

        if (stripped.startsWith("PREPMTH") && stripped.length == 15) {
            val p0 = stripped.substring(0, 4)
            val p1 = stripped.substring(4, 7)
            val p2 = stripped.substring(7, 11)
            val p3 = stripped.substring(11, 15)
            return "$p0-$p1-$p2-$p3"
        }

        if (stripped.startsWith("PREPPRO") && stripped.length == 15) {
            val p0 = stripped.substring(0, 4)
            val p1 = stripped.substring(4, 7)
            val p2 = stripped.substring(7, 11)
            val p3 = stripped.substring(11, 15)
            return "$p0-$p1-$p2-$p3"
        }

        if (stripped.startsWith("PREP") && stripped.length == 16) {
            val p0 = stripped.substring(0, 4)
            val p1 = stripped.substring(4, 8)
            val p2 = stripped.substring(8, 12)
            val p3 = stripped.substring(12, 16)
            return "$p0-$p1-$p2-$p3"
        }
        return clean
    }

    fun normalizeEmail(email: String): String {
        return if (email.isBlank()) "nobitanobi7209@gmail.com" else email.trim().lowercase(Locale.ROOT)
    }

    fun maskKey(key: String?): String {
        if (key.isNullOrBlank()) return "N/A"
        val cleaned = cleanKeyFormat(key)
        return if (cleaned.length >= 14) {
            cleaned.take(8) + "-••••-" + cleaned.takeLast(4)
        } else {
            "PREP-••••-••••"
        }
    }
}
