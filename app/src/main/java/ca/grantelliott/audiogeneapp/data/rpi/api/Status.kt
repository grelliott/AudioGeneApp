package ca.grantelliott.audiogeneapp.data.rpi.api

/**
 * Build this out to store data sent from the websocket connection
 * on the Raspberry Pi
 * It will eventually contain the parameters of the selected conductor
 * as well as the generation
 * It may also contain other values such as system temp, CPU, memory, etc
 */
data class Status (
    var status: String,
    var cpuStatus: Integer
)