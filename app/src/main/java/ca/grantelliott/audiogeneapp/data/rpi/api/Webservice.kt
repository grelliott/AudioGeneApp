package ca.grantelliott.audiogeneapp.data.rpi.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface Webservice {
    @ExperimentalCoroutinesApi
    suspend fun observeStatus(): StateFlow<Status>
    //TODO add in other send/receives for future interactions with server
}
