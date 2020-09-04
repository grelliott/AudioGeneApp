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
import ca.grantelliott.audiogeneapp.ui.components.GaugeComponent
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
        val rpiCpuGaugeView: GaugeComponent = view.findViewById(R.id.rpi_cpu_usage_gauge)
        val rpiMemGaugeView: GaugeComponent = view.findViewById(R.id.rpi_mem_usage_gauge)
        val rpiCpuTempView: GaugeComponent = view.findViewById(R.id.rpi_cpu_temp_gauge)

        lifecycleScope.launchWhenCreated {
            viewModel.status().observe(viewLifecycleOwner, {
                rpiStatusTextView.text = it.connectionStatus
                rpiCpuGaugeView.value = it.cpuUsage ?: 0f
                rpiMemGaugeView.value = it.memUsage ?: 0f
                rpiCpuTempView.value = it.cpuTemp ?: 0f
            })
        }
    }

    override fun onDestroy() {
        Timber.d("+onDestroy")
        super.onDestroy()
    }
}