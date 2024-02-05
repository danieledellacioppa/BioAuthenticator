package com.forteur.bioauthenticator

import androidx.lifecycle.MutableLiveData

class ViewModel (val activity: MainActivity) {
    var biometricManagerStatus: MutableLiveData<String> = MutableLiveData("")

    fun getFingerPrint() {
        val myBiometricManager = MyBiometricManager(activity, this)
        myBiometricManager.checkBiometricSupport()
        myBiometricManager.authenticate()
    }

    fun updateBiometricStatus(status: String) {
        biometricManagerStatus.value = status
    }
}