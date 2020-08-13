package ca.grantelliott.audiogeneapp.ui.supercollider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ca.grantelliott.audiogeneapp.R

class SuperColliderFragment : Fragment() {

    private lateinit var superColliderViewModel: SuperColliderViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        superColliderViewModel =
                ViewModelProviders.of(this).get(SuperColliderViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_supercollider, container, false)
        val textView: TextView = root.findViewById(R.id.text_supercollider)
        superColliderViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}