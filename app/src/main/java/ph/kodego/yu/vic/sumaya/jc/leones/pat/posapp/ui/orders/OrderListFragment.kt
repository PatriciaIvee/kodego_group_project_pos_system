package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.OrderListAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrderListBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderHistory
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderList
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.SwipeCallBack


class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private lateinit var orderListAdapter: OrderListAdapter
    private val viewModel: OrderListViewModel by activityViewModels()
    private var orders: ArrayList<Order> = ArrayList()
    private var ordersList: ArrayList<OrderList> = ArrayList()
    private lateinit var itemTouchHelper: ItemTouchHelper



    private var db = Firebase.firestore
    private var dbOrderHistory = Firebase.firestore
    private var uid = ""
    private val orderHistoryList: ArrayList<OrderHistory> = ArrayList()
    private var dbLatestOrder = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        orderListAdapter = OrderListAdapter(orders,requireActivity())
        binding.recyclerOrderList.adapter = orderListAdapter
        binding.recyclerOrderList.layoutManager = LinearLayoutManager(requireContext())


        //SWIPE ORDER LIST
        val swipeCallBack = SwipeCallBack(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipeCallBack.orderListAdapter = orderListAdapter
        itemTouchHelper = ItemTouchHelper(swipeCallBack)
        itemTouchHelper.attachToRecyclerView(binding.recyclerOrderList)

        //CHARGE BUTTON
        binding.btnCharge.setOnClickListener {
            orderHistoryList[0].served = true
            saveServedStatus()
            Toast.makeText(requireContext(),"Order Charged.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_nav_orders_order_list_to_nav_orders)
        }

        dbOrderHistory = FirebaseFirestore.getInstance()
        dbOrderHistory.collection("latestOrder").whereEqualTo("served",false).get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents){
                    val orderHistory: OrderHistory? = data.toObject(OrderHistory::class.java)
                    if (orderHistory != null) {
                        orderHistoryList.add(orderHistory)
                    }
                }
            }
            if (orderHistoryList.isNotEmpty()) {
                uid = orderHistoryList[0].uid.toString()
                db = FirebaseFirestore.getInstance()

                db.collection("order").whereEqualTo("uid", uid).get().addOnSuccessListener { it2->
                    if (!it2.isEmpty) {
                        for (data in it2.documents){
                            val order: OrderList? = data.toObject(OrderList::class.java)
                            if (order != null) {
                                ordersList.add(order)
                                orderListAdapter.notifyDataSetChanged()
                            }
                        }
                        var totalOrders = 0
                        var totalPrice = 0.0f
                        for (ordersOrders in ordersList) {
                            for (ordersOrdersOrders in ordersOrders.orderList) {
                                orders.add(ordersOrdersOrders)

                                totalOrders += ordersOrdersOrders.orderQuantity
                                totalPrice += ordersOrdersOrders.orderTotal

                            }
                        }
                        binding.textTotalQuantity.text = totalOrders.toString()
                        binding.textTotalAmount.text = totalPrice.toString()
                        binding.btnCharge.text = "Charge : $totalPrice"
                        viewModel.totalOrderQuantity.value = totalOrders
                    }
                }
                    .addOnFailureListener{
                        Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
                    }
            }
        }.addOnFailureListener{
//                Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
        }




        return binding.root
    }

    private fun saveServedStatus() {
        var orderHistoryMap = hashMapOf(
            "uid" to orderHistoryList[0].uid,
            "served" to orderHistoryList[0].served
        )

        dbLatestOrder.collection("latestOrder").document(uid).set(orderHistoryMap)
            .addOnSuccessListener {

            }
    }


//    private fun getTotalOrderQuantity(orders: List<Order>): Int {
//        var totalQuantity = 0
//        for (order in orders) {
//            totalQuantity += order.orderQuantity
//        }
//        return totalQuantity
//    }
//
//    private fun computeTotalItemAmount(orders: List<Order>): Float {
//        var totalAmount = 0.0f
//        for (order in orders) {
//            totalAmount += order.orderTotal
//        }
//        return totalAmount
//    }


}