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
        var myBiometricManager = MyBiometricManager(this)
        myBiometricManager.checkBiometricSupport()
        myBiometricManager.authenticate()

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