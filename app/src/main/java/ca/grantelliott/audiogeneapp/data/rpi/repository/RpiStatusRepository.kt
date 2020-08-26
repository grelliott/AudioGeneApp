package ca.grantelliott.audiogeneapp.data.rpi.repository

import android.util.Log
import ca.grantelliott.audiogeneapp.data.rpi.api.Subscribe
import ca.grantelliott.audiogeneapp.data.rpi.api.Webservice
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import okhttp3.OkHttpClient

class RpiStatusRepository {
    //TODO Assuming I can use some Dependency Injection here...
//    private val webservice: RpiWebservice = Retrofit.Builder()
//        .baseUrl("http://192.168.1.29")  //TODO replace with some configuration
//        .addConverterFactory(GsonConverterFactory.create())
//        .build().create(RpiWebservice::class.java)

    //TODO move to DI component class
    private val okHttpClient: OkHttpClient = OkHttpClient()
    private val scarletInstance: Scarlet = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory("ws://192.168.1.29:5000"))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
            .build()

    private val webservice: Webservice = scarletInstance.create<Webservice>()

    init {
        // Observe websocket events
        webservice.observeWebSocketEvent()
            .filter { it is WebSocket.Event.OnConnectionOpened<*> }
            .subscribe({
                val subscribe = Subscribe(
                    type = "subscribe"
                )
                webservice.sendSubscribe(subscribe)
            }, {
                e -> Log.e("TEST", "Error: ${e.message}")
            })

        // Listen for status message changes
        webservice.observeStatus()
            .subscribe({ rpiStatus ->
                Log.d("TEST", "Raspberry Pi Status is ${rpiStatus.status}")
            })
    }

}