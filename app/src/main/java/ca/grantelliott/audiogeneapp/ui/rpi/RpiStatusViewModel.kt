package ca.grantelliott.audiogeneapp.ui.rpi

import androidx.lifecycle.ViewModel
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.repository.RpiStatusRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RpiStatusViewModel @Inject constructor() : ViewModel() {
    private val  rpiStatusRepository: RpiStatusRepository = RpiStatusRepository()

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getStatus(): StateFlow<Status> {
        return rpiStatusRepository.observeStatus()
    }

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
//        rpiStatusRepository.onCleared()
    }
}