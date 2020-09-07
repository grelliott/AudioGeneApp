package ca.grantelliott.audiogeneapp.data.supercollider.repository

import ca.grantelliott.audiogeneapp.data.supercollider.connection.OscClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class SuperColliderRepository {
    private var oscClient: OscClient = OscClient()

    suspend fun connectClient() {
        withContext(Dispatchers.IO) {
            oscClient.connect()
        }
    }

    suspend fun disconnectClient() {
        withContext(Dispatchers.IO) {
            oscClient.disconnect()
        }
    }

    @ExperimentalCoroutinesApi
    fun observeVolume(): StateFlow<Float> {
        return oscClient.observeVolume()
    }

    suspend fun setVolume(volume: Float) {
        withContext(Dispatchers.IO) {
            oscClient.setVolume(volume)
        }
    }
}