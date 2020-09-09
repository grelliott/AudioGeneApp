package ca.grantelliott.audiogeneapp.data.supercollider.connection

import ca.grantelliott.audiogeneapp.data.supercollider.api.AudioControls
import com.illposed.osc.*
import com.illposed.osc.transport.udp.OSCPortIn
import com.illposed.osc.transport.udp.OSCPortInBuilder
import com.illposed.osc.transport.udp.OSCPortOut
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.net.*
import java.nio.channels.DatagramChannel

class OscClient: OSCPacketListener, AudioControls {
    //TODO use value from Settings
    // probably need to break up Rpi URL into components so the IP address
    // can be shared
    private val remoteAddr: InetAddress = InetAddress.getByName("192.168.1.29")
    private val remoteSocketAddress = InetSocketAddress(remoteAddr, 57120)
    private val sender: OSCPortOut = OSCPortOut (remoteSocketAddress)

    private lateinit var receiver: OSCPortIn

    @ExperimentalCoroutinesApi
    private val volumeState = MutableStateFlow(0f)

    fun connect() {
        Timber.d("+connect")
        if (!this::receiver.isInitialized) {
            initializeReceiver()
        }

        Timber.d("Connecting sender");
        sender.connect()

        //TODO send a subscribe message to start getting updates
        // might need to pass in port
        sender.send(OSCMessage("/subscribe", listOf(63571)))
    }

    /*
     * TODO trying to figure out why SC isn't sending OSC messages back
     *  using the following tshark command on server to test
     *  tshark -i wlan0 -O osc --enable-protocol osc -j udp -Y udp
     *
     * It looks like the SrcPort doesn't line up with what I'm trying to send from
     *
     * Frame 43: 62 bytes on wire (496 bits), 62 bytes captured (496 bits) on interface 0
     * Ethernet II, Src: 58:96:1d:95:04:d4 (58:96:1d:95:04:d4), Dst: Raspberr_8d:76:d9 (dc:a6:32:8d:7
     * Internet Protocol Version 4, Src: 192.168.1.137, Dst: 192.168.1.29
     * User Datagram Protocol, Src Port: 60891, Dst Port: 57120
     * Open Sound Control Encoding
     *     Message: /subscribe ,i
     *         Header
     *             Path: /subscribe
     *             Format: ,i
     *         Int32: 63571
     *
     * So probably need to spend some more time figuring out how to create a UDP port
     * we can listen on and receive messages from, and possibly use the port argument
     * in the OSC receiver to create the NetAddr with
     */
    private fun initializeReceiver() {
        Timber.d("+initializeReceiver")
        receiver = OSCPortInBuilder()
            .setLocalSocketAddress(InetSocketAddress(InetAddress.getLocalHost(), 63571))
            .setRemoteSocketAddress(InetSocketAddress(InetAddress.getLocalHost(), 63571))
            .setPacketListener(this)
            .build()

        Timber.d("Connecting receiver")
        receiver.connect()
    }

    fun disconnect() {
        if (sender.isConnected) {
            sender.send(OSCMessage("/unsubscribe"))
            sender.disconnect()
        }
    }

    override fun handlePacket(event: OSCPacketEvent?) {
        Timber.d("Received packet!")
    }

    override fun handleBadData(event: OSCBadDataEvent?) {
        Timber.w("Received bad data!")
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