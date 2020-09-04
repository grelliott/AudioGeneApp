package ca.grantelliott.audiogeneapp.ui.rpi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ca.grantelliott.audiogeneapp.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

class RpiStatusFragment : Fragment() {
    private val viewModel: RpiStatusViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("+onCreateView")
        return inflater.inflate(R.layout.rpi_status_fragment, container, false)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("+onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        val rpiStatusTextView: TextView = view.findViewById(R.id.text_rpi_status)
        val rpiCpuUSageTextView: TextView = view.findViewById(R.id.text_rpi_cpu_usage)

        lifecycleScope.launchWhenCreated {
            viewModel.getStatus().collect {
                rpiStatusTextView.text = it.status
                rpiCpuUSageTextView.text = DecimalFormat("0.0").format(it.cpuUsage)
            }
        }
    }

    override fun onDestroy() {
        Timber.d("+onDestroy")
        super.onDestroy()
    }
}