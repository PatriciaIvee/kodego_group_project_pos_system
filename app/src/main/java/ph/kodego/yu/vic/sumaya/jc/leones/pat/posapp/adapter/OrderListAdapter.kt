package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.OrderListItemBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order

class OrderListAdapter(var orders: ArrayList<Order>, var activity: Activity)
    : RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>(), Filterable {

    var filteredOrders:List<Order> = ArrayList<Order>()
    var all_orders = ArrayList<Order>()

    fun addOrder(order: Order){
        orders.add(0,order)
        notifyItemInserted(0)
    }

    fun removeOrder(position: Int){
        orders.removeAt(position)
        notifyItemRemoved(position)
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

        //NewFilterStudents CRUD
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

        private fun computeTotalItemQuantity(orders:List<Order>):Int{
            var totalQuantity = 0
            for (order in orders) {
                totalQuantity += order.orderQuantity
            }
            return totalQuantity
        }

        fun bindItem(order: Order){
            this.order = order

            //SET UI AND CLASS ITEM
            orderItem.itemName.text = order.itemName
            orderItem.itemCount.text = "x ${order.orderQuantity.toString()}"
            orderItem.itemAmount.text =" ${order.orderTotal}"

        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }

    //SEARCH
    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}