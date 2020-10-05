package com.example.globarstest.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.globarstest.data.Repository
import com.example.globarstest.data.models.Authorization
import com.example.globarstest.data.network.responsemodels.AuthorizationResponse
import com.example.globarstest.util.ConnectionStatus
import kotlinx.coroutines.*


class AuthorizationViewModel(
    private val repository: Repository,
    private val connectionStatus: ConnectionStatus
) : ViewModel() {
    val TOKEN_TAG = "token"

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var userName = ""
    private var password = ""

    private var _token = "Bearer "
    val token: String
        get() = _token

    private val _showNoInternetConnectionNotification = MutableLiveData<Boolean>()
    val showNoInternetConnectionMessage: LiveData<Boolean>
        get() = _showNoInternetConnectionNotification

    private val _authorizationResponse = MutableLiveData<AuthorizationResponse>()
    val authorizationResponse: LiveData<AuthorizationResponse>
        get() = _authorizationResponse

    private val _navigateToMap = MutableLiveData<Boolean>()
    val navigateToMap: LiveData<Boolean>
        get() = _navigateToMap

    private val _showErrorMessage = MutableLiveData<Boolean>()
    val showErrorMessage: LiveData<Boolean>
        get() = _showErrorMessage

    private val _hideKeyboard = MutableLiveData<Boolean>()
    val hideKeyBoard: LiveData<Boolean>
        get() = _hideKeyboard

    private val _enteredButtonDisabled = MutableLiveData<Boolean>()
    val enterButtonDisabled: LiveData<Boolean>
        get() = _enteredButtonDisabled

    fun onEnterButtonClicked() {
        login()
    }

    fun onEditUserNameTextChanged(text: String) {
        userName = text
        if (userName.isEmpty() or password.isEmpty()) _enteredButtonDisabled.value = true
    }

    fun afterEditUserNameTextChanged() {
        _enteredButtonDisabled.value = userName.isEmpty() or password.isEmpty()
    }

    fun onEditPasswordTextChanged(text: String) {
        password = text
        if (userName.isEmpty() or password.isEmpty()) _enteredButtonDisabled.value = true
    }

    fun afterEditPasswordTextChanged() {
        _enteredButtonDisabled.value = userName.isEmpty() or password.isEmpty()
    }

    private fun login() {
        if (!checkConnectionStatus()) return
        val auth = Authorization(userName, password)
        viewModelScope.launch {
            _authorizationResponse.postValue(repository.login(auth))
        }
        _hideKeyboard.value = true
    }

    private fun checkConnectionStatus(): Boolean {
        if (!connectionStatus.isOnline()) {
            _showNoInternetConnectionNotification.value = true
            return false
        }
        return true
    }

    fun checkUserNameAndPassword() {
        if (_authorizationResponse.value != null) {
            if (_authorizationResponse.value!!.success) {
                _token += _authorizationResponse.value!!.data
                _navigateToMap.value = true
            } else _showErrorMessage.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancel()
    }
}