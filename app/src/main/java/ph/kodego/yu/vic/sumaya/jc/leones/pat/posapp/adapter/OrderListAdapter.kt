package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.OrderListItemBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderHistory
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderList
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders.OrderListViewModel

class OrderListAdapter(var orders: ArrayList<Order>, var activity: Activity)
    : RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>() {

    var filteredOrders:List<Order> = ArrayList<Order>()
    private var all_orders = ArrayList<Order>()
    private var dbOrderHistory = Firebase.firestore
    private val orderHistoryList: ArrayList<OrderHistory> = ArrayList()
    private var uid: String = ""
    private var dbRef= Firebase.firestore
    private lateinit var viewModel: OrderListViewModel


    fun addOrder(order: Order){
        orders.add(0,order)
        notifyItemInserted(0)
        viewModel.totalOrderQuantity.value = getTotalOrderQuantity(orders)
    }

    private fun getTotalOrderQuantity(orders: List<Order>): Int {
        var totalQuantity = 0
        for (order in orders) {
            totalQuantity += order.orderQuantity
        }
        return totalQuantity
    }

    fun removeOrder(position: Int){
        orders.removeAt(position)
        notifyItemRemoved(position)
        saveChanges()
//        viewModel = OrderListViewModel()
//        viewModel.totalOrderQuantity.value = getTotalOrderQuantity(orders)
    }

    private fun saveChanges() {
        dbOrderHistory = FirebaseFirestore.getInstance()

        dbOrderHistory.collection("latestOrder").whereEqualTo("served",false).get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents){
                    val orderHistory: OrderHistory? = data.toObject(OrderHistory::class.java)
                    if (orderHistory != null) {
                        orderHistoryList.add(orderHistory)
                        uid = orderHistoryList[0].uid.toString()
                        var orderMap = hashMapOf(
                            "uid" to uid,
                            "orderList" to orders
                        )
                        dbRef.collection("order").document(uid).set(orderMap)
                            .addOnSuccessListener {
                            }
                    }
                }
            }
        }.addOnFailureListener{
//                Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
        }
    }


    fun updateOrder(newOrders:ArrayList<Order>){
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return orders.size
    }


    override fun onCreateViewHolder(
//        Sets layout per line (row)
        parent: ViewGroup,
        viewtype: Int
    ):OrderListAdapter.OrderListViewHolder {

        //NewFilterOrders CRUD
        all_orders.clear()
        all_orders.addAll(orders)

        val orderItemBinding = OrderListItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,false)
        return OrderListViewHolder(orderItemBinding)
    }

    override fun onBindViewHolder(holder :OrderListAdapter.OrderListViewHolder,
                                  position:Int){
//        what's inside each layout (item name)

        holder.bindItem(orders[position])
//        pass the data to be sent to viewHolder
    }


    inner class OrderListViewHolder(private val orderItem: OrderListItemBinding)
        : RecyclerView.ViewHolder(orderItem.root), View.OnClickListener {

        var order = Order()


        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem(order: Order){
            this.order = order

            //SET UI AND CLASS ITEM
            orderItem.itemName.text = order.itemName
            orderItem.itemCount.text = "x ${order.orderQuantity.toString()}"
            orderItem.itemAmount.text =" ${order.orderTotal}"

        }

        override fun onClick(v: View?) {
            Toast.makeText(activity,"you clicked your order", Toast.LENGTH_SHORT).show()
        }
    }

}