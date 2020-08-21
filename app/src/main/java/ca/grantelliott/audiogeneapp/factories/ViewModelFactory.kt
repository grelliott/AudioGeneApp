package ca.grantelliott.audiogeneapp.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ca.grantelliott.audiogeneapp.data.repository.RpiStatusRepository
import ca.grantelliott.audiogeneapp.ui.rpi.RpiStatusViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val repository: RpiStatusRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RpiStatusViewModel::class.java!!)) {
            RpiStatusViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel not found")
        }
    }
}