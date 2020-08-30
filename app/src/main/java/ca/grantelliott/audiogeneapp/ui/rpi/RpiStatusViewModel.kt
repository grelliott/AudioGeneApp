package ca.grantelliott.audiogeneapp.ui.rpi

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.repository.RpiStatusRepository

class RpiStatusViewModel @ViewModelInject constructor() : ViewModel() {
    private val rpiStatusRepository: RpiStatusRepository = RpiStatusRepository()
    val status: LiveData<Status> = LiveDataReactiveStreams.fromPublisher(rpiStatusRepository.observeStatus())

    override fun onCleared() {
        super.onCleared()
        rpiStatusRepository.onCleared()
    }
}