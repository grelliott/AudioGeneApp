package ca.grantelliott.audiogeneapp.data.rpi.connection

import android.util.Log
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.api.Webservice
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Response
import okhttp3.WebSocketListener

class RpiWebSocketListener : WebSocketListener(), Webservice {

    @ExperimentalCoroutinesApi
    val statusStream = MutableStateFlow(Status("Disconnected", 0.0f))
    private var gson = Gson()

    @ExperimentalCoroutinesApi
    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
        Log.d("TEST", "Socket opened")
        statusStream.value = Status("Connected", 0.0f)
    }

    @ExperimentalCoroutinesApi
    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
        Log.d("TEST", "Received message: $text")
        statusStream.value = gson.fromJson(text, Status::class.java)
    }

    @ExperimentalCoroutinesApi
    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        Log.d("TEST","Socket closing: $reason")
        statusStream.value = Status("Closing", 0.0f)
    }

    @ExperimentalCoroutinesApi
    override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
        Log.d("TEST", "Socket closed: $reason")
        statusStream.value = Status("Closed", 0.0f)
    }

    @ExperimentalCoroutinesApi
    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
        Log.d("Test", "Socket failure: ${t.message}")
        statusStream.value = Status("Failure", 0.0f)
    }

    @ExperimentalCoroutinesApi
    override fun observeStatus(): StateFlow<Status> {
        Log.d("TEST", "+Listener.observeStatus")
        return statusStream
    }
}