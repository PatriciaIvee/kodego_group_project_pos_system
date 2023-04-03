package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private var actionOrderListBadge: TextView? = null
    //badgeCount for items to be charged in orderListFragment (sample)
    private var badgeCount = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.order_fragment_menu, menu)


        val menuItem = menu.findItem(R.id.action_order_list)
        menuItem.setActionView(R.layout.order_fragment_action_image_badge)


        val actionView = menuItem.actionView
        if (actionView != null) {
            actionOrderListBadge = actionView.findViewById(R.id.action_order_list_badge)
            updateBadge(menu)
            actionView.setOnClickListener {
                // Navigate to the orderList fragment
                findNavController().navigate(R.id.action_nav_orders_to_orderListFragment)
            }
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


    private fun updateBadge(menu: Menu ) {

        // Get the action_order_list_badge TextView
        val menuItem = menu.findItem(R.id.action_order_list)
        val actionView = menuItem.actionView
        if (actionView != null) {
            actionOrderListBadge = actionView.findViewById(R.id.action_order_list_badge)

            if (badgeCount > 0) {
                // Show the badge if the count is greater than 0
                actionOrderListBadge?.text = badgeCount.toString()
                actionOrderListBadge?.visibility = View.VISIBLE
            } else {
                // Hide the badge if the count is 0
                actionOrderListBadge?.visibility = View.GONE
            }
        }
    }

}