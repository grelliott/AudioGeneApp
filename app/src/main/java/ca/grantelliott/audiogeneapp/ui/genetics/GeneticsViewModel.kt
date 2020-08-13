package ca.grantelliott.audiogeneapp.ui.genetics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GeneticsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is genetics Fragment"
    }
    val text: LiveData<String> = _text
}