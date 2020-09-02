package ca.grantelliott.audiogeneapp.data.rpi.api

import ca.grantelliott.audiogeneapp.R

enum class SystemStatus(val statusResourceId: Int) {
    DISCONNECTED(R.string.disconnected),
    CONNECTING(R.string.connecting),
    CONNECTED(R.string.connected),
    RUNNING(R.string.running),
    ERROR(R.string.error),
    FAILURE(R.string.failure)
}