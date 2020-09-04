package ca.grantelliott.audiogeneapp.data.rpi.connection

import ca.grantelliott.audiogeneapp.data.rpi.api.*
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Response
import okhttp3.WebSocketListener
import timber.log.Timber

class RpiWebSocketListener : WebSocketListener(), Webservice {
    @ExperimentalCoroutinesApi
    private val statusStream = MutableStateFlow(DISCONNECTED_STATUS)
    private var gson = Gson()

    @ExperimentalCoroutinesApi
    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
        Timber.d("Socket opened")
        statusStream.value = CONNECTED_STATUS
    }

    @ExperimentalCoroutinesApi
    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
        Timber.d("Received message: $text")
        statusStream.value = gson.fromJson(text, Status::class.java)
    }

    @ExperimentalCoroutinesApi
    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        Timber.d("Socket closing: $reason")
        statusStream.value = CLOSING_STATUS
    }

    @ExperimentalCoroutinesApi
    override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        Timber.d("Socket closed: $reason")
        statusStream.value = CLOSED_STATUS
    }

    @ExperimentalCoroutinesApi
    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
        Timber.d("Socket failure: ${t.message}")
        statusStream.value = CONNECTION_FAILURE_STATUS
    }

    @ExperimentalCoroutinesApi
    override suspend fun observeStatus(): StateFlow<Status> {
        Timber.d("+observeStatus")
        return statusStream
    }
}