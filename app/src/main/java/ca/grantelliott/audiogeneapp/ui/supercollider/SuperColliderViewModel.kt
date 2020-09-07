package ca.grantelliott.audiogeneapp.ui.supercollider

import androidx.lifecycle.*
import ca.grantelliott.audiogeneapp.data.supercollider.repository.SuperColliderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SuperColliderViewModel : ViewModel() {
    private val repository: SuperColliderRepository = SuperColliderRepository()

    init {
        Timber.d("+init")
        viewModelScope.launch {
            repository.connectClient()
        }
    }

    @ExperimentalCoroutinesApi
    private var _volumeState: LiveData<Float> = liveData {
        repository.observeVolume().collect {
            emit(it)
        }
    }

    @ExperimentalCoroutinesApi
    fun volume(): LiveData<Float> {
        return _volumeState
    }

    suspend fun volume(volume: Float) {
        repository.setVolume(volume)
    }

}