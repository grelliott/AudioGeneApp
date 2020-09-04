package ca.grantelliott.audiogeneapp.data.rpi.connection

import ca.grantelliott.audiogeneapp.data.rpi.api.*
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Response
import okhttp3.WebSocketListener
import timber.log.Timber

class RpiWebSocketListener : WebSocketListener(), Webservice {
    @ExperimentalCoroutinesApi
    private val statusStream = MutableStateFlow(UNKNOWN_STATUS)

    @ExperimentalCoroutinesApi
    private val connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)

    private var gson = GsonBuilder().registerTypeAdapter(Status::class.java, StatusDeserializer()).create()

    @ExperimentalCoroutinesApi
    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
        Timber.d("Socket opened")
        connectionState.value = ConnectionState.CONNECTED
    }

    @ExperimentalCoroutinesApi
    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
        Timber.d("Received message: $text")
        statusStream.value = gson.fromJson(text, Status::class.java)
    }

    @ExperimentalCoroutinesApi
    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        Timber.d("Socket closing: $reason")
        connectionState.value = ConnectionState.CLOSING
    }

    @ExperimentalCoroutinesApi
    override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        Timber.d("Socket closed: $reason")
        connectionState.value = ConnectionState.CLOSED
    }

    @ExperimentalCoroutinesApi
    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
        Timber.d("Socket failure: ${t.message}")
        connectionState.value = ConnectionState.FAILURE
    }

    @ExperimentalCoroutinesApi
    override suspend fun observeStatus(): StateFlow<Status> {
        Timber.d("+observeStatus")
        return statusStream
    }

    @ExperimentalCoroutinesApi
    fun observeConnectionState(): StateFlow<ConnectionState> {
        Timber.d("+observeConnectionState")
        return connectionState
    }
}