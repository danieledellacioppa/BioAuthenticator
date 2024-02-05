package com.forteur.bioauthenticator

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.util.concurrent.Executor

class MyBiometricManager(private val context: Context, viewModel: ViewModel) {
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val executor: Executor = ContextCompat.getMainExecutor(context)

    init {
        setupBiometricPrompt(viewModel)
    }

    private fun setupBiometricPrompt(viewModel: ViewModel) {
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Gestisci l'errore di autenticazione
                Log.e("MY_APP_TAG", "Errore di autenticazione: $errString")
                viewModel.updateBiometricStatus("Errore di autenticazione: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                // Gestisci il successo di autenticazione
                Log.d("MY_APP_TAG", "Autenticazione riuscita")
//                viewModel.updateBiometricStatus("Autenticazione riuscita")
                // aggiorna il valore di biometricManagerStatus con il numero di millisecondi trascorsi dal 1 gennaio 1970
                viewModel.updateBiometricStatus(System.currentTimeMillis().toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Gestisci il fallimento di autenticazione
                Log.d("MY_APP_TAG", "Autenticazione fallita")
                viewModel.updateBiometricStatus("Autenticazione fallita")
            }
        }

        biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, callback)

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticazione Biometrica")
            .setSubtitle("Accedi utilizzando le tue impronte digitali")
            .setNegativeButtonText("Annulla")
            .build()
    }

    fun checkBiometricSupport(): Boolean {
        val canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        return when (canAuthenticate) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "Biometric success.")
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED,
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.e("MY_APP_TAG", "Il dispositivo non supporta la biometria o ci sono problemi di configurazione.")
                false
            }
            else -> false
        }
    }

    fun authenticate() {
        if (checkBiometricSupport()) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            Log.e("MY_APP_TAG", "Autenticazione biometrica non supportata o configurata correttamente.")
        }
    }
}

