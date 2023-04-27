package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.OrderItemGridLayoutBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Item

class ItemAdapter (var items: ArrayList<Item>, var activity: Activity)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(), Filterable {

    var filteredItems:List<Item> = ArrayList<Item>()
    var all_records = ArrayList<Item>()

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
            itemBinding.itemPrice.text = "$ ${item.itemPrice.toString()}"

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

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }

    //SEARCH
    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}