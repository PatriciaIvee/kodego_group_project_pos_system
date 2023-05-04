package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter

import android.app.Activity
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.OrderItemGridLayoutBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Item
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderHistory
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderList
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders.OrderListViewModel
import java.time.LocalDateTime

class ItemAdapter (var items: ArrayList<Item>, var activity: Activity)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(), Filterable {

    var filteredItems:List<Item> = ArrayList<Item>()
    var all_records = ArrayList<Item>()

    private val orders: ArrayList<Order> = ArrayList()
    private var dbRef= Firebase.firestore
    private var uid: String = ""
    private var order: Order = Order()
    private var dbLatestOrder = Firebase.firestore
    private var orderHistory: OrderHistory = OrderHistory()
    private var dbOrderHistory = Firebase.firestore
    private val orderHistoryList: ArrayList<OrderHistory> = ArrayList()
    private var db = Firebase.firestore
    private var ordersList: ArrayList<OrderList> = ArrayList()
    private lateinit var viewModel: OrderListViewModel



    fun addItem(item: Item){
        items.add(0,item)
        notifyItemInserted(0)
    }

    fun removeItem(position: Int){
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItems(newItems:ArrayList<Item>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(
//        Sets layout per line (row)
        parent: ViewGroup,
        viewtype: Int
    ):ItemAdapter.ItemViewHolder {

        //NewFilterStudents CRUD
        all_records.clear()
        all_records.addAll(items)

        val itemBinding = OrderItemGridLayoutBinding//ItemAccountBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,false)

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
                var new : Boolean = true
                uid = orderHistoryList[0].uid.toString()
                db = FirebaseFirestore.getInstance()

                db.collection("order").whereEqualTo("uid", uid).get().addOnSuccessListener { it2->
                    if (!it2.isEmpty) {
                        for (data in it2.documents){
                            val order: OrderList? = data.toObject(OrderList::class.java)
                            if (order != null) {
                                ordersList.add(order)
                            }
                        }
                        for (ordersOrders in ordersList) {
                            for (ordersOrdersOrders in ordersOrders.orderList) {
                                for (orders4 in orders) {
                                    if (ordersOrdersOrders.itemName == orders4.itemName) {
                                        new = false
                                        break
                                    }
                                }
                                if (new) {
                                    orders.add(ordersOrdersOrders)
                                }
                            }
                        }
                    }
                }
                    .addOnFailureListener{
                        Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
                    }
            } else {
                uid = dbRef.collection("order").document().id
            }
        }.addOnFailureListener{
//                Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
        }

        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder :ItemAdapter.ItemViewHolder,
                                  position:Int){
//        what's inside each layout (item name)

        holder.bindItem(items[position])
//        pass the data to be sent to viewHolder
    }


    inner class ItemViewHolder(private val itemBinding: OrderItemGridLayoutBinding)
        :RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

        var item = Item()

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem(item: Item){
            this.item = item

            //SET UI AND CLASS ITEM
            itemBinding.itemName.text = item.itemName
            itemBinding.textStock.text = "${item.itemStock.toString()} available"
            itemBinding.itemPrice.text = "₱ ${item.itemPrice.toString()}"

            //SET THE IMAGE
            itemBinding.imgItem.setImageResource(item.img)
//                to set picture from internet source
//                Bitmap = new Bit
//                itemBinding.profilePicture.resources

            //itemBinding.deleteRowButton.setOnClickListener{
//                Snackbar.make(itemBinding.root,
//                    "Delete by Button",
//                    Snackbar.LENGTH_SHORT
//                ).show()
//
//                var dao : ItemDAO = ItemDAOSQLImpl(activity.applicationContext)
//                dao.deleteItem(item.itemId)
//
//                removeItem(adapterPosition)
//            }

        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onClick(v: View?) {
            var new: Boolean = true
            order = Order(item.itemName, item.itemPrice, item.img)
            orderHistory = OrderHistory(uid,false)



            if (orders.isEmpty()) {
                orders.add(order)
                orders[0].orderId = uid
                orders[0].orderQuantity = 1
                orders[0].orderTotal = order.itemPrice
            } else {
                for (newOrder in orders) {
                    if (newOrder.itemName == order.itemName) {
                        newOrder.orderQuantity++
                        newOrder.orderTotal = (newOrder.itemPrice * newOrder.orderQuantity).toFloat()
                        new = false
                        break
                    }
//                    else {
//                        orderList.add(0,order)
//                        orderList[0].orderId = uid
//                        orderList[0].orderQuantity = 1
//                        Toast.makeText(activity, "test", Toast.LENGTH_SHORT).show()
//                    }
                }
                if (new == true) {
                    orders.add(0,order)
                    orders[0].orderId = uid
                    orders[0].orderQuantity = 1
                }
            }

            saveOrder()

        }

        private fun saveOrder() {
            var orderHistoryMap = hashMapOf(
                "uid" to orderHistory.uid,
                "served" to orderHistory.served
            )
            var orderMap = hashMapOf(
                "uid" to uid,
                "orderList" to orders
            )

            dbLatestOrder.collection("latestOrder").document(uid).set(orderHistoryMap)
                .addOnSuccessListener {

                }
            dbRef.collection("order").document(uid).set(orderMap)
                .addOnSuccessListener {
                    Toast.makeText(activity, "Ordered ${item.itemName} ,${item.itemPrice}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}
