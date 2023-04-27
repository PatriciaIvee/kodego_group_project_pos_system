package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.ItemAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.OrderListAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentItemListBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentItemsBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrderListBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Item
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order


class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemAdapter: ItemAdapter
    private var items: ArrayList<Item> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)


        itemAdapter = ItemAdapter(items,requireActivity())
        binding.recyclerItems.adapter = itemAdapter
        binding.recyclerItems.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }


}