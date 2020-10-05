package com.example.globarstest.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.globarstest.data.Repository
import com.example.globarstest.data.models.Vehicle
import com.example.globarstest.util.ConnectionStatus
import kotlinx.coroutines.*

class MapViewModel(
    private val repository: Repository,
    private val connectionStatus: ConnectionStatus
) : ViewModel() {
    val TOKEN_TAG = "token"

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var token = ""
    private val _showNoInternetConnectionNotification = MutableLiveData<Boolean>()

    val showNoInternetConnectionMessage: LiveData<Boolean>
        get() = _showNoInternetConnectionNotification
    private val _vehicles = MutableLiveData<List<Vehicle>>()
    val vehicles: LiveData<List<Vehicle>>
        get() = _vehicles

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String>
        get() = _sessionId

    fun getSessionId(token: String) {
        if (!checkConnectionStatus()) return
        this.token = token
        viewModelScope.launch {
            _sessionId.postValue(repository.getSessionId(token))
        }
    }

    fun loadVehicles() {
        if (!checkConnectionStatus()) return
        viewModelScope.launch {
            _vehicles.postValue(
                repository.getVehiclesList(token, _sessionId.value!!).filter { it.checked })
        }
    }

    private fun checkConnectionStatus(): Boolean {
        if (!connectionStatus.isOnline()) {
            _showNoInternetConnectionNotification.value = true
            return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancel()
    }
}
