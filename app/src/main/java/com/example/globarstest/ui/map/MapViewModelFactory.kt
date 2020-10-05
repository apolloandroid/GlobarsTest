package com.example.globarstest.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.globarstest.data.Repository
import com.example.globarstest.util.ConnectionStatus

class MapViewModelFactory(
    private val repository: Repository,
    private val connectionStatus: ConnectionStatus
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(repository, connectionStatus) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
