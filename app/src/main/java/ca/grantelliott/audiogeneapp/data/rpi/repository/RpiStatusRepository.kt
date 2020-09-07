package ca.grantelliott.audiogeneapp.data.rpi.repository

import ca.grantelliott.audiogeneapp.data.rpi.api.ConnectionState
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
    private var httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .build()
    private var wsListener: RpiWebSocketListener = RpiWebSocketListener()

    private lateinit var request: Request
    private lateinit var webSocket: WebSocket

    @ExperimentalCoroutinesApi
    suspend fun observeStatus(): StateFlow<Status> {
        Timber.d("+observeStatus")
        return wsListener.observeStatus()
    }

    @ExperimentalCoroutinesApi
    fun observeConnectionStatus() :StateFlow<ConnectionState> {
        return wsListener.observeConnectionState()
    }

    fun connect(ipAddress: String) {
        Timber.d("+connect IP address = $ipAddress")
        request = Request.Builder()
            .url("ws://$ipAddress:5000/status")
            .build()
        webSocket = httpClient.newWebSocket(request, wsListener)
    }

    fun disconnect() {
        Timber.d("+disconnect")
        webSocket.close(1001, "Closed")
    }
}
