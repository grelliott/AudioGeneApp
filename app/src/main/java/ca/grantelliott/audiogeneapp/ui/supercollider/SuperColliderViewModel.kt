package ca.grantelliott.audiogeneapp.ui.supercollider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SuperColliderViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is SuperCollider Fragment"
    }
    val text: LiveData<String> = _text
}