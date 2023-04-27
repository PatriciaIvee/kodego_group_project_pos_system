package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.ItemAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrdersBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Item

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private var actionOrderListBadge: TextView? = null

    private val viewModel: OrderListViewModel by activityViewModels()

    private lateinit var itemAdapter: ItemAdapter
    private var items: ArrayList<Item> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Load the OrderListFragment in the background
        //Because the badge will only load when the OrderListFragment is opened
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(OrderListFragment(), "orderListFragment")
        fragmentTransaction.commitNow()

        // Retrieve the total order quantity from the OrderListViewModel
        val totalOrderQuantity = viewModel.totalOrderQuantity.value

        // Use the total order quantity as needed
        if (totalOrderQuantity != null && totalOrderQuantity > 0) {
            actionOrderListBadge?.text = totalOrderQuantity.toString()
            actionOrderListBadge?.visibility = View.VISIBLE
        } else {
            actionOrderListBadge?.visibility = View.GONE
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        itemAdapter = ItemAdapter(items,requireActivity())
        binding.recyclerItem.adapter = itemAdapter
        binding.recyclerItem.layoutManager = GridLayoutManager(requireContext(),3)

        updateBadge()
        //ADDS STRING ARRAY FROM STRING RESOURCES
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.item_options_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                _binding!!.itemSpinner.adapter = adapter
            }
        }
        //DO SOMETHING WHEN OPTIONS FROM ARRAY IS SELECTED
        _binding!!.itemSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Perform actions when an option is selected
                val selectedItem = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Perform actions when nothing is selected
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.order_fragment_menu, menu)

        val menuItem = menu.findItem(R.id.action_order_list)
        menuItem.setActionView(R.layout.order_fragment_action_image_badge)

        val actionView = menuItem.actionView
        actionOrderListBadge = actionView!!.findViewById(R.id.action_order_list_badge)
        // Update the badge with the current total order quantity
        if (viewModel.totalOrderQuantity.value!! > 0) {
            actionOrderListBadge?.text = viewModel.totalOrderQuantity.value.toString()
            actionOrderListBadge?.visibility = View.VISIBLE
        } else {
            actionOrderListBadge?.visibility = View.GONE
        }

        actionView.setOnClickListener {
            // Navigate to the orderList fragment
            findNavController().navigate(R.id.action_nav_orders_to_orderListFragment)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    //THIS DOESN'T WORK ANYMORE AFTER ADDING ACTION ORDER LIST BADGE
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_order_list -> {
//                // Create a new instance of the checkout fragment
//                // Navigate to the orderList fragment
//                findNavController().navigate(R.id.action_nav_orders_to_orderListFragment)
//                true // indicate that the event was handled
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    //badge does not immediately update when I start the app.
    // Only updates after opening OrderListFragment
    private fun updateBadge() {
        viewModel.totalOrderQuantity.observe(viewLifecycleOwner) { totalOrderQuantity ->
            actionOrderListBadge?.let { badge ->
                if (totalOrderQuantity > 0) {
                    badge.text = totalOrderQuantity.toString()
                    badge.visibility = View.VISIBLE
                } else {
                    badge.visibility = View.GONE
                }
            }
        }
    }


    private fun init(){
        items.add(Item("Item 1", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 2", 200.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 3", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 4", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 5", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 6", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 7", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 8", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 9", 100.0f,R.drawable.ic_baseline_image_24))
        items.add(Item("Item 10", 100.0f,R.drawable.ic_baseline_image_24))
    }
}