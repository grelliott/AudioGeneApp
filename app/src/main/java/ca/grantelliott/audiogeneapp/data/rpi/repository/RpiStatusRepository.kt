package ca.grantelliott.audiogeneapp.data.rpi.repository

import android.util.Log
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.connection.RpiWebSocketListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RpiStatusRepository @Inject constructor() {
    private val rpiUrl: String = "ws://192.168.1.29:5000/status"

    //TODO move to DI component class
    private val request: Request = Request.Builder()
        .url(rpiUrl)
        .build()
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(0,TimeUnit.SECONDS)
        .connectTimeout(0, TimeUnit.SECONDS)
        .build()

    private val wsListener: RpiWebSocketListener = RpiWebSocketListener()
    private val webSocket = httpClient.newWebSocket(request, wsListener)

    @ExperimentalCoroutinesApi
    fun observeStatus(): StateFlow<Status> {
        Log.d("TEST", "+observeStatus")
        return wsListener.observeStatus()
    }
}
