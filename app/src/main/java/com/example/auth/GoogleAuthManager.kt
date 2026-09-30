package com.example.auth

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

sealed class GoogleAuthResult {
    data class Success(val idToken: String) : GoogleAuthResult()
    object Cancelled : GoogleAuthResult()
    data class NoAccountFound(val message: String) : GoogleAuthResult()
    data class Error(val message: String) : GoogleAuthResult()
}

class GoogleAuthManager(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    suspend fun signIn(): GoogleAuthResult {
        return try {
            // Strictly use the Web / Server OAuth 2.0 client ID (client_type 3)
            val serverClientId = try {
                context.getString(R.string.default_web_client_id)
            } catch (e: Exception) {
                "450443043040-10lru4mk769su32us990aejidg9lai8d.apps.googleusercontent.com"
            }

            // Build Google Identity option without custom hashed nonce so Firebase Auth
            // processes the token seamlessly without nonce-mismatch errors
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                GoogleAuthResult.Success(googleIdTokenCredential.idToken)
            } else {
                GoogleAuthResult.Error("Unexpected credential type returned from Google.")
            }
        } catch (e: GetCredentialCancellationException) {
            GoogleAuthResult.Cancelled
        } catch (e: NoCredentialException) {
            GoogleAuthResult.NoAccountFound(
                "No Google account found on this device or emulator. The browser Preview runs on a clean emulator without a Google account. Please add a Google account in Android Settings or test on a physical Android device."
            )
        } catch (e: GetCredentialException) {
            val msg = e.localizedMessage ?: ""
            if (msg.contains("No credentials available", ignoreCase = true) ||
                msg.contains("no credential", ignoreCase = true) ||
                e.type == "android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL"
            ) {
                GoogleAuthResult.NoAccountFound(
                    "No Google account found on this device or emulator. The browser Preview runs on a clean emulator without a Google account. Please add a Google account in Android Settings or test on a physical Android device."
                )
            } else {
                GoogleAuthResult.Error(msg.ifEmpty { "Google Sign-In failed." })
            }
        } catch (e: Exception) {
            val msg = e.localizedMessage ?: ""
            if (msg.contains("No credentials available", ignoreCase = true)) {
                GoogleAuthResult.NoAccountFound(
                    "No Google account found on this device or emulator. The browser Preview runs on a clean emulator without a Google account. Please add a Google account in Android Settings or test on a physical Android device."
                )
            } else {
                GoogleAuthResult.Error(msg.ifEmpty { "An unexpected error occurred during Google Sign-In." })
            }
        }
    }

    /**
     * Opens Android System settings so the user can add a Google account if running on an emulator.
     */
    fun openAddAccountSettings() {
        try {
            val intent = Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            try {
                val fallback = Intent(Settings.ACTION_SYNC_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallback)
            } catch (_: Exception) {}
        }
    }
}
