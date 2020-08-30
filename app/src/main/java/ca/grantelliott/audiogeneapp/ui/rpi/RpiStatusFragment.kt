package ca.grantelliott.audiogeneapp.ui.rpi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import ca.grantelliott.audiogeneapp.R

class RpiStatusFragment : Fragment() {
    private val viewModel: RpiStatusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       return inflater.inflate(R.layout.rpi_status_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rpiStatusTextView: TextView = view.findViewById<TextView>(R.id.text_rpi_status)
        viewModel.status.observe(viewLifecycleOwner, {
            rpiStatusTextView.text = it.status
        })
    }
}