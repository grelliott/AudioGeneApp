package ca.grantelliott.audiogeneapp.data.rpi.repository

import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.connection.RpiWebSocketListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RpiStatusRepository @Inject constructor() {
    private lateinit var request: Request
    private lateinit var httpClient: OkHttpClient
    private lateinit var wsListener: RpiWebSocketListener
    private lateinit var webSocket: WebSocket

    @ExperimentalCoroutinesApi
    suspend fun observeStatus(): StateFlow<Status> {
        Timber.d("+observeStatus")
        return wsListener.observeStatus()
    }

    fun connect() {
        Timber.d("+connect")
        httpClient = OkHttpClient.Builder()
            .readTimeout(0,TimeUnit.SECONDS)
            .connectTimeout(0, TimeUnit.SECONDS)
            .build()
        request = Request.Builder()
            .url(Companion.RPI_URL)
            .build()
        wsListener = RpiWebSocketListener()
        webSocket = httpClient.newWebSocket(request, wsListener)
    }

    fun disconnect() {
        Timber.d("+disconnect")
        webSocket.close(1001, "Closed")
    }

    companion object {
        private const val RPI_URL: String = "ws://192.168.1.29:5000/status"
    }
}
