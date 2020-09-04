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

        return Status(systemStatus, cpuUsage, memUsage, cpuTemp)
    }
}

val UNKNOWN_STATUS = Status(null, null, null, null)