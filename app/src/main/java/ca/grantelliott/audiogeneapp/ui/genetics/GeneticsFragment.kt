package ca.grantelliott.audiogeneapp.ui.genetics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ca.grantelliott.audiogeneapp.R

class GeneticsFragment : Fragment() {

    private lateinit var geneticsViewModel: GeneticsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        geneticsViewModel =
                ViewModelProviders.of(this).get(GeneticsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_genetics, container, false)
        val textView: TextView = root.findViewById(R.id.text_genetics)
        geneticsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}