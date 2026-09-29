package com.example.data.security

import java.security.MessageDigest
import java.util.Locale

object ActivationKeySecurity {
    // Secret internal salt ensuring cryptographic uniqueness
    private const val SECRET_MASTER_SALT = "PREP_TRACK_PRO_ACTIVATION_KEY_SECURE_SALT_2026_V1"

    /**
     * Official pool of 25 unique Master Activation Keys (Keys 1 to 25).
     * These keys are kept by the app owner to sell/distribute to users.
     * When any user applies one of these keys, it is permanently bound to their specific Account ID.
     */
    val MASTER_KEYS: List<String> = listOf(
        "PREP-PRO-9841-7A2F", // Key 01
        "PREP-PRO-3194-E8BC", // Key 02
        "PREP-PRO-5629-4D1A", // Key 03
        "PREP-PRO-8713-C95E", // Key 04
        "PREP-PRO-2408-B63F", // Key 05
        "PREP-PRO-6952-1E7D", // Key 06
        "PREP-PRO-4187-F30A", // Key 07
        "PREP-PRO-7360-9B4C", // Key 08
        "PREP-PRO-1594-A82D", // Key 09
        "PREP-PRO-8246-3C7E", // Key 10
        "PREP-PRO-3701-E59B", // Key 11
        "PREP-PRO-6489-2A1F", // Key 12
        "PREP-PRO-9135-D74C", // Key 13
        "PREP-PRO-5820-F63E", // Key 14
        "PREP-PRO-2974-8B1A", // Key 15
        "PREP-PRO-7603-C49D", // Key 16
        "PREP-PRO-4358-1E2B", // Key 17
        "PREP-PRO-1892-A76F", // Key 18
        "PREP-PRO-8527-3D0C", // Key 19
        "PREP-PRO-6214-E98A", // Key 20
        "PREP-PRO-3940-5F1B", // Key 21
        "PREP-PRO-7186-B32E", // Key 22
        "PREP-PRO-5409-8C7D", // Key 23
        "PREP-PRO-2631-4A9E", // Key 24
        "PREP-PRO-9758-F10C"  // Key 25
    )

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
     * Validates whether the entered activation key is strictly valid for this user's account ID (email).
     * Validates against either the official 25 Master Activation Keys OR the cryptographic per-account key.
     */
    fun validateKey(enteredKey: String, email: String): Boolean {
        val normalizedEntered = cleanKeyFormat(enteredKey)
        if (MASTER_KEYS.any { it.equals(normalizedEntered, ignoreCase = true) }) {
            return true
        }
        val expected = generateKeyForUser(email)
        return normalizedEntered.equals(expected, ignoreCase = true)
    }

    /**
     * Normalizes and cleans the key string (e.g. trims whitespace, uppercase, fixes dashes)
     */
    fun cleanKeyFormat(key: String): String {
        val clean = key.trim().uppercase(Locale.ROOT)
        val stripped = clean.replace("-", "").replace(" ", "")

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
