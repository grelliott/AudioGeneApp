package ca.grantelliott.audiogeneapp.data.supercollider.connection

import ca.grantelliott.audiogeneapp.data.supercollider.api.AudioControls
import com.illposed.osc.*
import com.illposed.osc.transport.udp.OSCPortOut
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.net.InetAddress
import java.net.InetSocketAddress

class OscClient: OSCPacketListener, AudioControls {
    //TODO use value from Settings
    // probably need to break up Rpi URL into components so the IP address
    // can be shared
    private val remoteAdd: InetAddress = InetAddress.getByName("192.168.1.29")
    private val remoteSocketAddress = InetSocketAddress(remoteAdd, 57120)
    private val sender: OSCPortOut = OSCPortOut(remoteSocketAddress)

    @ExperimentalCoroutinesApi
    private val volumeState = MutableStateFlow(0f)

    fun connect() {
        Timber.d("+connect")
        sender.connect()
        //TODO send a subscribe message to start getting updates
        // might need to pass in port
        sender.send(OSCMessage("/subscribe", listOf(0)))
    }

    fun disconnect() {
        if (sender.isConnected) {
            sender.send(OSCMessage("/unsubscribe"))
            sender.disconnect()
        }
    }

    override fun handlePacket(event: OSCPacketEvent?) {
        TODO("Not yet implemented")
    }

    override fun handleBadData(event: OSCBadDataEvent?) {
        TODO("Not yet implemented")
    }

    @ExperimentalCoroutinesApi
    override fun observeVolume(): StateFlow<Float> {
        return volumeState
    }

    override fun setVolume(volume: Float) {
        if (sender.isConnected) {
            sender.send(OSCMessage("/ctrl/gain", listOf(volume)))
        }
    }
}