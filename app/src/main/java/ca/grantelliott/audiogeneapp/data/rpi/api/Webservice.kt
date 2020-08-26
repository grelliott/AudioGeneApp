package ca.grantelliott.audiogeneapp.data.rpi.api

import ca.grantelliott.audiogeneapp.data.rpi.api.Status
import ca.grantelliott.audiogeneapp.data.rpi.api.Subscribe
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface Webservice {
    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>
    @Send
    fun sendSubscribe(subscribe: Subscribe)
    @Receive
    fun observeStatus(): Flowable<Status>

    //TODO add in other send/receives for future interactions with server
}
