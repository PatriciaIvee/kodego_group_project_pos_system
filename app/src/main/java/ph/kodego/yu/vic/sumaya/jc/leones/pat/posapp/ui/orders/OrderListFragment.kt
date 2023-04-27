package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.OrderListAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrderListBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.SwipeCallBack


class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderListAdapter: OrderListAdapter
    private val viewModel: OrderListViewModel by activityViewModels()
    private var orders: ArrayList<Order> = ArrayList()
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        orderListAdapter = OrderListAdapter(orders,requireActivity())
        binding.recyclerOrderList.adapter = orderListAdapter
        binding.recyclerOrderList.layoutManager = LinearLayoutManager(requireContext())

        //UPDATE TOTAL AMOUNT
        binding.textTotalAmount.text = computeTotalItemAmount(orders).toString()
        //UPDATE TOTAL ORDER QUANTITY
        binding.textTotalQuantity.text = "${getTotalOrderQuantity(orders).toString()} items"

        viewModel.totalOrderQuantity.value = getTotalOrderQuantity(orders)

        //SWIPE ORDER LIST
        val swipeCallBack = SwipeCallBack(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipeCallBack.orderListAdapter = orderListAdapter
        itemTouchHelper = ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerOrderList)

        //CHARGE BUTTON
        binding.btnCharge.setOnClickListener {
            // TODO: GET OrderNo. Items everything about the transaction/order
            //TODO: SEND TO RECEIPTS
            Toast.makeText(requireContext(),"Order Charged.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun getTotalOrderQuantity(orders: List<Order>): Int {
        var totalQuantity = 0
        for (order in orders) {
            totalQuantity += order.orderQuantity
        }
        return totalQuantity
    }

    private fun computeTotalItemAmount(orders: List<Order>): Float {
        var totalAmount = 0.0f
        for (order in orders) {
            totalAmount += order.orderTotal
        }
        return totalAmount
    }

    //TOTAL 40 ITEMS
    private fun init(){
        orders.add(Order("Item 1", 100.0f,R.drawable.ic_baseline_image_24).apply {
            orderQuantity = 1
        })
        orders.add(Order("Item 2", 200.0f,R.drawable.ic_baseline_image_24).apply {
            orderQuantity = 5
        })
        orders.add(Order("Item 3", 100.0f,R.drawable.ic_baseline_image_24).apply {
            orderQuantity = 23
        })
        orders.add(Order("Item 4", 100.0f,R.drawable.ic_baseline_image_24).apply {
            orderQuantity = 3
        })
        orders.add(Order("Item 5", 100.0f,R.drawable.ic_baseline_image_24).apply {
            orderQuantity = 8
        })
    }


}