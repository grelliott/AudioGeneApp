package ca.grantelliott.audiogeneapp.ui.rpi

import androidx.lifecycle.*
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.repository.RpiStatusRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class RpiStatusViewModel: ViewModel() {
    private val rpiStatusRepository: RpiStatusRepository = RpiStatusRepository()
    @ExperimentalCoroutinesApi
    private val _status: LiveData<Status> = liveData {
        rpiStatusRepository.observeStatus().collect {
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

    override fun onCleared() {
        Timber.d("+onCleared")
        super.onCleared()
        rpiStatusRepository.disconnect()
    }
}