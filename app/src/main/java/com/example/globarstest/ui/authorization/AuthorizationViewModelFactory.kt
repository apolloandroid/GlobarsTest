package com.example.globarstest.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.globarstest.data.Repository
import com.example.globarstest.util.ConnectionStatus

class AuthorizationViewModelFactory(
    private val repository: Repository,
    private val connectionStatus: ConnectionStatus
) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthorizationViewModel::class.java)) {
            return AuthorizationViewModel(repository, connectionStatus) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}