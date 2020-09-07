package ca.grantelliott.audiogeneapp.ui.rpi

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import ca.grantelliott.audiogeneapp.R
import ca.grantelliott.audiogeneapp.data.rpi.api.ConnectionState
import ca.grantelliott.audiogeneapp.data.rpi.api.RunningState
import ca.grantelliott.audiogeneapp.ui.components.GaugeComponent
import ca.grantelliott.audiogeneapp.ui.supercollider.SuperColliderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

class RpiStatusFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    //TODO get viewmodel by DI and provide it SharedPreferences
    private val viewModel: RpiStatusViewModel by viewModels()

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)
        viewModel.updateDataSource(sharedPrefs.getString("rpi_ip_address", "ws://192.168.1.29:5000/status")!!)
    }

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

        //TODO move to MainActivity
        val navItem = activity?.findViewById<BottomNavigationView>(R.id.nav_view)
        val homeBadge = navItem?.getOrCreateBadge(R.id.navigation_home)
        val geneticsBadge = navItem?.getOrCreateBadge(R.id.navigation_genetics)
        val supercolliderBadge = navItem?.getOrCreateBadge(R.id.navigation_supercollider)

        val rpiCpuGaugeView: GaugeComponent = view.findViewById(R.id.rpi_cpu_usage_gauge)
        val rpiMemGaugeView: GaugeComponent = view.findViewById(R.id.rpi_mem_usage_gauge)
        val rpiCpuTempView: GaugeComponent = view.findViewById(R.id.rpi_cpu_temp_gauge)

        lifecycleScope.launchWhenCreated {
            viewModel.connectionStatus().observe(viewLifecycleOwner, {
                homeBadge?.backgroundColor = when (it) {
                    ConnectionState.CONNECTED -> activity?.getColor(R.color.colorGaugeGood)!!
                    else -> activity?.getColor(R.color.colorGaugeCritical)!!
                }
            })
            viewModel.status().observe(viewLifecycleOwner, {
                rpiCpuGaugeView.value = it.cpuUsage
                rpiMemGaugeView.value = it.memUsage
                rpiCpuTempView.value = it.cpuTemp

                if (it.systemStatus != null) {
                    geneticsBadge?.backgroundColor =
                        when (it.systemStatus?.audiogene) {
                            RunningState.RUNNING -> activity?.getColor(R.color.colorGaugeGood)!!
                            else -> activity?.getColor(R.color.colorGaugeCritical)!!
                        }
                    supercolliderBadge?.backgroundColor =
                        when (it.systemStatus?.scsynth) {
                            RunningState.RUNNING -> activity?.getColor(R.color.colorGaugeGood)!!
                            else -> activity?.getColor(R.color.colorGaugeCritical)!!
                        }
                } else {
                    navItem?.getOrCreateBadge(R.id.navigation_genetics)?.backgroundColor = activity?.getColor(R.color.colorGaugeCritical)!!
                    navItem?.getOrCreateBadge(R.id.navigation_supercollider)?.backgroundColor = activity?.getColor(R.color.colorGaugeCritical)!!
                }
            })
        }  // end lifecycle
    }

    override fun onDestroy() {
        Timber.d("+onDestroy")
        super.onDestroy()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences?, key: String?) {
        if (prefs != null) {
            Timber.d("Shared preference changed. Key = $key")
            if (key == "rpi_ip_address") {
                val dataSourceUri = prefs.getString(key, "")!!
                Timber.d("Setting data source to $dataSourceUri")
                viewModel.updateDataSource(dataSourceUri)
            }
        }
    }
}