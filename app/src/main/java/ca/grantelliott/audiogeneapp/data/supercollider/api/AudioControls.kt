package ca.grantelliott.audiogeneapp.data.supercollider.api

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

interface AudioControls {
    @ExperimentalCoroutinesApi
    fun observeVolume(): StateFlow<Float>
    fun setVolume(volume: Float)
}