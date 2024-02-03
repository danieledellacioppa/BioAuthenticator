package com.forteur.bioauthenticator

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.forteur.bioauthenticator.ui.theme.BioAuthenticatorTheme
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {
    var biometricManagerStatus : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("MY_APP_TAG", "Biometric success.")
                biometricManagerStatus = "Biometric success."
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.e("MY_APP_TAG", "Il dispositivo non ha un sensore biometrico.")
                biometricManagerStatus = "Il dispositivo non ha un sensore biometrico."
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.e("MY_APP_TAG", "Il sensore biometrico non è attualmente disponibile.")
                biometricManagerStatus = "Il sensore biometrico non è attualmente disponibile."
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("MY_APP_TAG", "Non ci sono biometrie registrate.")
                biometricManagerStatus = "Non ci sono biometrie registrate."
            }

            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                Log.e("MY_APP_TAG", "Il dispositivo richiede un aggiornamento della sicurezza.")
                biometricManagerStatus = "Il dispositivo richiede un aggiornamento della sicurezza."
            }

            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                Log.e("MY_APP_TAG", "Il dispositivo non supporta la biometria.")
                biometricManagerStatus = "Il dispositivo non supporta la biometria."
            }

            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                Log.e("MY_APP_TAG", "Impossibile determinare lo stato della biometria.")
                biometricManagerStatus = "Impossibile determinare lo stato della biometria."
            }
        }

        // Configura l'autenticazione biometrica qui
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = androidx.biometric.BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "Errore di autenticazione: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "Autenticazione riuscita", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Autenticazione fallita", Toast.LENGTH_SHORT).show()
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticazioneae Biometricae")
            .setSubtitle("Accedie utilizzande le tue impronte digitale e se puoie ramme pure nu passagge")
            .setNegativeButtonText("Annulla")
            .build()

        // Mostra il prompt biometrico all'avvio dell'Activity, o in base alla tua logica
        biometricPrompt.authenticate(promptInfo)

        setContent {
            BioAuthenticatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BiometryStatus(biometricManagerStatus)
                }
            }
        }
    }
}

@Composable
fun BiometryStatus(biometricManagerStatus: String) {
    Text(text = biometricManagerStatus)
}