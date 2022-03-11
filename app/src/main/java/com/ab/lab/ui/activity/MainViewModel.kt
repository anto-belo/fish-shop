package com.ab.lab.ui.activity

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

class MainViewModel : ViewModel() {

    var account: GoogleSignInAccount? = null

}