package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.CategoryItemBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Category

class ItemCategoryAdapter(var categories: ArrayList<Category>,  var activity: Activity)
    : RecyclerView.Adapter<ItemCategoryAdapter.CategoryListViewHolder>(),
    Filterable {

    var filteredCategory:List<Category> = ArrayList<Category>()
    var all_Categories = ArrayList<Category>()

    fun addCategory(category: Category){
        categories.add(0,category)
        notifyItemInserted(0)
    }

    fun removeCategory(position: Int){
        categories.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateOrder(newCategory:ArrayList<Category>){
        categories.clear()
        categories.addAll(newCategory)
        notifyDataSetChanged()

    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onCreateViewHolder(
//        Sets layout per line (row)
        parent: ViewGroup,
        viewtype: Int
    ):ItemCategoryAdapter.CategoryListViewHolder {

        //NewFilterOrders CRUD
        all_Categories.clear()
        all_Categories.addAll(categories)

        //Layout of the category item(row in recyclerView)
        val categoryItemBinding = CategoryItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,false)
        return CategoryListViewHolder(categoryItemBinding)
    }

    override fun onBindViewHolder(holder :ItemCategoryAdapter.CategoryListViewHolder,
                                  position:Int){
//        what's inside each layout (item name)

        holder.bindItem(categories[position])
//        pass the data to be sent to viewHolder
    }

    inner class CategoryListViewHolder(private val categoryItem: CategoryItemBinding)
        : RecyclerView.ViewHolder(categoryItem.root), View.OnClickListener {

        var category = Category()

        init {
            itemView.setOnClickListener(this)
        }

        fun bindItem(category: Category){
            this.category = category

            //SET UI AND CLASS ITEM
            categoryItem.categoryName.text = category.categoryName
            categoryItem.imgCategory.setImageResource(category.img)

        }

        override fun onClick(v: View?) {

            //WHEN THE CATEGORY IS CLICKED THE SPINNER SHOULD CHANGE TO THE SAME CATEGORY
            //FRAGMENT SHOULD SHOW ALL THE ITEMS FROM THE CATEGORY
            //THE BOTTOM SHEET DIALOG WILL BE HIDDEN
            val bottomSheetBehavior = BottomSheetBehavior.from(activity.findViewById(R.id.bottom_dialogue))
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

        }

    }

    //SEARCH
    //NOT SURE HOW TO USE THIS
    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}