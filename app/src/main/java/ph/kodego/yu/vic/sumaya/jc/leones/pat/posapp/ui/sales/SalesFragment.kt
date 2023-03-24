package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.sales

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrdersBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentSalesBinding

class SalesFragment : Fragment() {

    private var _binding: FragmentSalesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}