package ca.grantelliott.audiogeneapp.data.rpi.api

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type

/**
 * Build this out to store data sent from the websocket connection
 * on the Raspberry Pi
 * It will eventually contain the parameters of the selected conductor
 * as well as the generation
 * It may also contain other values such as system temp, CPU, memory, etc
 */

enum class ConnectionState {
    DISCONNECTED,
    CONNECTED,
    CLOSING,
    CLOSED,
    FAILURE
}

enum class RunningState {
    NOT_RUNNING,
    RUNNING,
    UNKNOWN
}

data class SystemStatus (
    var audiogene: RunningState,
    var scsynth: RunningState
)

data class Status(
    var connectionStatus: ConnectionState,
    var systemStatus: SystemStatus?,
    var cpuUsage: Float?,
    var memUsage: Float?,
    var cpuTemp: Float?
)

class StatusDeserializer: JsonDeserializer<Status> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Status {
        json as JsonObject
        val connectionStatus = ConnectionState.CONNECTED

        val audiogeneStatus = when(json.get("systemStatus").asJsonObject.get("audiogene").asString) {
            "Running" -> RunningState.RUNNING
            "Not Running" -> RunningState.NOT_RUNNING
            else -> RunningState.UNKNOWN
        }

        val scsynthStatus = when(json.get("systemStatus").asJsonObject.get("scsynth").asString) {
            "Running" -> RunningState.RUNNING
            "Not Running" -> RunningState.NOT_RUNNING
            else -> RunningState.UNKNOWN
        }
        val systemStatus = SystemStatus(audiogeneStatus, scsynthStatus)
        val cpuUsage = json.get("cpuUsage").asFloat
        val memUsage = json.get("memUsage").asFloat
        val cpuTemp = json.get("cpuTemp").asFloat

        return Status(connectionStatus, systemStatus, cpuUsage, memUsage, cpuTemp)
    }
}

val DISCONNECTED_STATUS = Status(ConnectionState.DISCONNECTED, null, null, null, null)
val CONNECTED_STATUS = Status(ConnectionState.CONNECTED, null, null, null, null)
val CLOSING_STATUS = Status(ConnectionState.CLOSING, null, null, null, null)
val CLOSED_STATUS = Status(ConnectionState.CLOSED, null, null, null, null)
val CONNECTION_FAILURE_STATUS = Status(ConnectionState.FAILURE, null, null, null, null)