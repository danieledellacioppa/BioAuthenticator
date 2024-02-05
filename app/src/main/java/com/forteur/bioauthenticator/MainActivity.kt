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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.forteur.bioauthenticator.ui.theme.BioAuthenticatorTheme
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {
    lateinit var viewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModel(this)
        viewModel.getFingerPrint()

        setContent {
            BioAuthenticatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BiometryStatus(viewModel)
                }
            }
        }
    }
}

@Composable
fun BiometryStatus(viewModel: ViewModel) {
    // Osserva i cambiamenti di LiveData in Compose
    val status = viewModel.biometricManagerStatus.observeAsState()
    Text(text = "Biometric Status: ${status.value}")
}