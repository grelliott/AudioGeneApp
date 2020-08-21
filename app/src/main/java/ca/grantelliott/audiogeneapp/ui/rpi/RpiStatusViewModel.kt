package ca.grantelliott.audiogeneapp.ui.rpi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ca.grantelliott.audiogeneapp.data.api.RpiStatus
import ca.grantelliott.audiogeneapp.data.repository.RpiStatusRepository
import javax.inject.Inject

//TODO probably need to create a factory to provide this model with the parameter
class RpiStatusViewModel @Inject constructor(
    rpiStatusRepository: RpiStatusRepository
) : ViewModel() {
    // Look into the JetPack tutorials
    // This should get updated based on the status of a Tinder websocket connection result
    // Then the data can be presented using some view
    // This should probably be placed somewhere else, will figure out organization later

    val status: LiveData<RpiStatus> = rpiStatusRepository.getStatus()
}