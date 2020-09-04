package ca.grantelliott.audiogeneapp.ui.rpi

import androidx.lifecycle.*
import ca.grantelliott.audiogeneapp.data.rpi.api.ConnectionState
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.repository.RpiStatusRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.sql.Connection

class RpiStatusViewModel: ViewModel() {
    private val rpiStatusRepository: RpiStatusRepository = RpiStatusRepository()

    @ExperimentalCoroutinesApi
    private val _status: LiveData<Status> = liveData {
        rpiStatusRepository.observeStatus().collect {
            emit(it)
        }
    }
    @ExperimentalCoroutinesApi
    private val _connectionState: LiveData<ConnectionState> = liveData {
        rpiStatusRepository.observeConnectionStatus().collect {
            emit(it)
        }
    }

    init {
        viewModelScope.launch {
            rpiStatusRepository.connect()
        }
    }

    @ExperimentalCoroutinesApi
    fun status(): LiveData<Status> {
        Timber.d("+status")
        return _status
    }

    @ExperimentalCoroutinesApi
    fun connectionStatus(): LiveData<ConnectionState> {
        return _connectionState
    }

    override fun onCleared() {
        Timber.d("+onCleared")
        super.onCleared()
        rpiStatusRepository.disconnect()
    }
}