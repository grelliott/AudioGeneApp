package ca.grantelliott.audiogeneapp.data.rpi.api

/**
 * Build this out to store data sent from the websocket connection
 * on the Raspberry Pi
 * It will eventually contain the parameters of the selected conductor
 * as well as the generation
 * It may also contain other values such as system temp, CPU, memory, etc
 */

data class SystemStatus (
    var audiogene: String,
    var scsynth: String
)

data class Status(
    var connectionStatus: String,
    var systemStatus: SystemStatus?,
    var cpuUsage: Float?,
    var memUsage: Float?,
    var cpuTemp: Float?
)

val DISCONNECTED_STATUS = Status("Disconnected", null, null, null, null)
val CONNECTED_STATUS = Status("Connected", null, null, null, null)
val CLOSING_STATUS = Status("Closing", null, null, null, null)
val CLOSED_STATUS = Status("Closed", null, null, null, null)
val CONNECTION_FAILURE_STATUS = Status("Failure", null, null, null, null)