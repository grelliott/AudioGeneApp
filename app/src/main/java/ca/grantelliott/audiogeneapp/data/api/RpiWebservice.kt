package ca.grantelliott.audiogeneapp.data.api

import retrofit2.Call
import retrofit2.http.GET

//TODO replace basic web service call with websocket call
//import com.tinder.scarlet.WebSocket
//import com.tinder.scarlet.ws.Receive
//import com.tinder.scarlet.ws.Send
//import io.reactivex.Flowable
//
//interface RpiWebservice {
//    @Receive
//    fun observeWebSocketEvent(): Flowable<WebSocket.Event>
//    @Send
//    fun sendSubscribe(subscribe: Subscribe)
//    @Receive
//    fun observeStatus(): Flowable<RpiStatus>
//}

interface RpiWebservice {
    @GET("status")
    fun getStatus(): Call<RpiStatus>
}