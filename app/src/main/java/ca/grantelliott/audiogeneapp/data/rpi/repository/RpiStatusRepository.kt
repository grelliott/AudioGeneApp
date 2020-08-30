package ca.grantelliott.audiogeneapp.data.rpi.repository

import android.util.Log
import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.api.Subscribe
import ca.grantelliott.audiogeneapp.data.rpi.api.Webservice
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import javax.inject.Inject

class RpiStatusRepository @Inject constructor() {
    private val disposables = CompositeDisposable()

    private val rpiUrl: String = "ws://192.168.1.29:5000/status"
    //TODO move to DI component class
    private val webservice: Webservice = Scarlet.Builder()
        .webSocketFactory(OkHttpClient().newWebSocketFactory(rpiUrl))
        .addMessageAdapterFactory(GsonMessageAdapter.Factory())
        .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
        .build()
        .create<Webservice>()

    init {
        Log.d("TEST", "Init repository")
        disposables.add(webservice.observeWebSocketEvent()
            .subscribeOn(Schedulers.io())
            .filter { it is WebSocket.Event.OnConnectionOpened<*> }
            .subscribe(
                {
                //TODO update status to CONNECTED
                Log.d("TEST", "Connection opened")
                webservice.sendSubscribe(Subscribe())
                },
                { e ->
                    Log.e("TEST", "Error: ${e.message}")
                })
        )
    }

    fun observeStatus(): Flowable<Status> {
        return webservice.observeStatus()
    }

    fun onCleared() {
        disposables.clear()
    }
}
