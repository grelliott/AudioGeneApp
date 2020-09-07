package ca.grantelliott.audiogeneapp.ui.supercollider

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ca.grantelliott.audiogeneapp.R
import com.google.android.material.slider.Slider

class SuperColliderControlsFragment : Fragment() {
    private val supercolliderViewModel: SuperColliderViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_super_collider_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO change to vertical slider
        val gainSlider: Slider = view.findViewById(R.id.volume_slider)
        gainSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                //noop
            }

            override fun onStopTrackingTouch(slider: Slider) {
                lifecycleScope.launchWhenStarted {
                    supercolliderViewModel.volume(slider.value)
                }
            }
        })

        //TODO add equalizer
        //TODO add cutoffs
    }
}