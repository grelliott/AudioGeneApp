package ca.grantelliott.audiogeneapp.ui.rpi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import ca.grantelliott.audiogeneapp.R
import ca.grantelliott.audiogeneapp.factories.ViewModelFactory
import ca.grantelliott.audiogeneapp.data.rpi.repository.RpiStatusRepository

class RpiStatusFragment : Fragment() {

//    companion object {
//        fun newInstance() = RpiStatusFragment()
//    }

    //TODO need to get this somehow?
    private val rpiStatusRepository: RpiStatusRepository = RpiStatusRepository()
    private val viewModelFactory: ViewModelFactory = ViewModelFactory(rpiStatusRepository)

    private lateinit var viewModel: RpiStatusViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, this.viewModelFactory).get(RpiStatusViewModel::class.java)
       return inflater.inflate(R.layout.rpi_status_fragment, container, false)

//        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView: TextView = view.findViewById<TextView>(R.id.text_rpi_status)
        viewModel.status.observe(viewLifecycleOwner, {
            textView.text = it.status
        })
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(RpiStatusViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}