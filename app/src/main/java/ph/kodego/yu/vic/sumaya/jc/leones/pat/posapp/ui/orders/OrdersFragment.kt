package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.ItemAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentOrdersBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Item
import java.util.*
import kotlin.collections.ArrayList

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private var actionOrderListBadge: TextView? = null
    private var actionViewBackgroundBadge: ImageView? = null

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
                val category = parent?.getItemAtPosition(position) as String
                val filteredItems = if (category == "All Items") {
                    items
                } else {
                    items.filter { it.category == category }
                }

                if (filteredItems.isEmpty()) {
                    binding.textNoItemsAvailable.visibility = View.VISIBLE
                } else {
                    binding.textNoItemsAvailable.visibility = View.GONE
                }

                itemAdapter.items = filteredItems as ArrayList<Item>

                itemAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Perform actions when nothing is selected
            }
        }

        binding.btnSearch.setOnClickListener {
            val searchText = binding.searchBarInput.text.toString().toLowerCase(Locale.getDefault())
            val filteredItems = items.filter {
                it.itemName
                    .toLowerCase(Locale.getDefault())
                    .contains(searchText) }
            itemAdapter.items = filteredItems as ArrayList<Item>
            itemAdapter.notifyDataSetChanged()

        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.order_fragment_menu, menu)

        val menuItem = menu.findItem(R.id.action_order_list)
        menuItem.setActionView(R.layout.order_fragment_action_image_badge)

        val actionView = menuItem.actionView
        actionOrderListBadge = actionView!!.findViewById(R.id.action_order_list_badge)
        actionViewBackgroundBadge = actionView!!.findViewById(R.id.circle_image)
        // Update the badge with the current total order quantity
        updateBadge()

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
                    badge.visibility = View.INVISIBLE
                }
            }
            actionViewBackgroundBadge?.let {
                if (totalOrderQuantity > 0) {
                    it.visibility = View.VISIBLE
                } else {
                    it.visibility = View.INVISIBLE
                }
            }
        }
    }


    private fun init(){
        var item = Item("Extra Sauce", 20.0f, R.drawable.ic_baseline_image_24)
        item.itemId = 1
        item.description = "Add More Sauce."
        item.category = "Add-Ons"
        item.itemStock = 10
        items.add(item)

        item = Item("Extra Shot", 20.0f,R.drawable.ic_baseline_image_24)
        item.itemId = 2
        item.description = "Add More Shot."
        item.category = "Add-Ons"
        item.itemStock = 10
        items.add(item)

        item =Item("Extra Syrup", 20.0f,R.drawable.ic_baseline_image_24)
        item.itemId = 3
        item.description = "Add more Syrup."
        item.category = "Add-Ons"
        item.itemStock = 10
        items.add(item)

        item =Item("Americano", 100.0f,R.drawable.coffee)
        item.itemId = 4
        item.description = "Espresso with Hot Water"
        item.category = "Coffee"
        item.itemStock = 10
        items.add(item)

        item = Item("Cappuccino", 100.0f,R.drawable.coffee)
        item.itemId = 5
        item.description = "Espresso, Steamed Milk and Foam."
        item.category = "Coffee"
        item.itemStock = 10
        items.add(item)

        item = Item("Butterscotch Latte", 100.0f,R.drawable.latte)
        item.itemId = 6
        item.description = "Espresso, Steamed Milk and Foam and Butterscotch."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Caramel Latte", 100.0f,R.drawable.latte)
        item.itemId = 7
        item.description = "Espresso, Steamed Milk and Foam drizzled with Caramel."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Flat White", 100.0f,R.drawable.latte)
        item.itemId = 7
        item.description = "Micro foamed milk  with shots of espresso"
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("French Vanilla Latte", 100.0f,R.drawable.latte)
        item.itemId = 8
        item.description = "Espresso, Steamed Whole Milk and French Vanilla Syrup."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Hazelnut Latte", 100.0f,R.drawable.hazelnut_latte)
        item.itemId = 9
        item.description = "Espresso, Steamed Milk and Foam with hazelnuts."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Macadamia Latte", 100.0f,R.drawable.latte)
        item.itemId = 10
        item.description = "Espresso, Steamed Milk and Foam added with Macadamia nuts."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Mocha Latte", 100.0f,R.drawable.latte)
        item.itemId = 11
        item.description = "Espresso with chocolate syrup and cream."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Sea Salt Latte", 100.0f,R.drawable.sea_salt_latte)
        item.itemId = 12
        item.description = "Espresso combined with sea salt, steamed milk and foam."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Spanish Latte", 100.0f,R.drawable.latte)
        item.itemId = 13
        item.description = "Espresso, Condensed milk and steamed milk."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Vanilla Latte", 100.0f,R.drawable.vanilla_latte)
        item.itemId = 14
        item.description = "Espresso, Steamed Milk and Foam drizzled with Vanilla."
        item.category = "Latte"
        item.itemStock = 10
        items.add(item)

        item = Item("Choco", 100.0f,R.drawable.choco)
        item.itemId = 15
        item.description = "Chocolate Goodness"
        item.category = "Non-Coffee"
        item.itemStock = 10
        items.add(item)

        item = Item("Choco Hazelnut", 100.0f,R.drawable.non_coffee)
        item.itemId = 16
        item.description = "Chocolate Bursting with Hazelnuts"
        item.category = "Non-Coffee"
        item.itemStock = 10
        items.add(item)

        item = Item("Matcha", 100.0f,R.drawable.matcha)
        item.itemId = 17
        item.description = "Matcha sourced from the locals."
        item.category = "Non-Coffee"
        item.itemStock = 10
        items.add(item)

        item = Item("Strawberry Matcha", 100.0f,R.drawable.strawberry_matcha)
        item.itemId = 18
        item.description = "Matcha combined with the best strawberries"
        item.category = "Non-Coffee"
        item.itemStock = 10
        items.add(item)

        item = Item("Strawberry Milk", 100.0f,R.drawable.strawberry_milk)
        item.itemId = 19
        item.description = "Strawberry added with farm milk."
        item.category = "Non-Coffee"
        item.itemStock = 10
        items.add(item)


        item = Item("Apple Pie", 100.0f,R.drawable.pastries)
        item.itemId = 20
        item.description = "Pie with sweet apples."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Banana Muffin", 100.0f,R.drawable.banana_muffin)
        item.itemId = 20
        item.description = "Muffin with bananas."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Blueberry Cheesecake", 100.0f,R.drawable.pastries)
        item.itemId = 21
        item.description = "Cheesecake loaded with bluberries."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Choco Butternut Crinkles", 100.0f,R.drawable.pastries)
        item.itemId = 22
        item.description = "Chewy and sweet crinkles dunked on butternut powder."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Cookies", 100.0f,R.drawable.pastries)
        item.itemId = 23
        item.description = "Cookies from grandma's recipe."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Creamy Butterscotch", 100.0f,R.drawable.pastries)
        item.itemId = 24
        item.description = "Butter and brown sugar that has been slowly heated together to create a soft-crack candy."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Croissant", 100.0f,R.drawable.pastries)
        item.itemId = 25
        item.description = "Laminated, yeast-leavened bakery product that contains dough/roll-in fat layers to create a flaky, crispy texture."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Oatmeal Cookies", 100.0f,R.drawable.oatmeal_cookies)
        item.itemId = 26
        item.description = "Soft chewy cookies with oatmeal and a touch of cinnamon."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Red Velvet Bar", 100.0f,R.drawable.red_velvet_bar)
        item.itemId = 27
        item.description = "Decadent, delicious, and topped with the best cream cheese frosting!"
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)

        item = Item("Red Velvet Crinkles", 100.0f,R.drawable.red_velvet_crinkles)
        item.itemId = 28
        item.description = "Dense, fudgy, chocolate cookie rolled in powdered sugar that forms a distinctly crackled top."
        item.category = "Pastries"
        item.itemStock = 10
        items.add(item)


        item = Item("Caramel Macchiato", 100.0f,R.drawable.ic_baseline_image_24)
        item.itemId = 29
        item.description = "Vanilla syrup, steamed milk, espresso, and caramel sauce."
        item.category = "Specials"
        item.itemStock = 10
        items.add(item)

        item = Item("Matcha Espresso Fusion", 100.0f,R.drawable.matcha_espresso_fusion)
        item.itemId = 30
        item.description = "Espresso Roast with fine Matcha powder and milk."
        item.category = "Specials"
        item.itemStock = 10
        items.add(item)

        item = Item("Salted Caramel", 100.0f,R.drawable.salted_caramel)
        item.itemId = 31
        item.description = "Soft sweet food made from heated sugar and butter or cream, with salt added as a flavouring"
        item.category = "Specials"
        item.itemStock = 10
        items.add(item)

        item = Item("Salted Caramel Macchiato", 100.0f,R.drawable.salted_caramel_macchiato)
        item.itemId = 32
        item.description = "Sweet and salty flavor with frothed milk, homemade salted caramel sauce and espresso."
        item.category = "Specials"
        item.itemStock = 10
        items.add(item)

        item = Item("Salted White Mocha", 100.0f,R.drawable.salted_white_mocha)
        item.itemId = 33
        item.description = "Blend of chocolates, salted caramel "
        item.category = "Specials"
        item.itemStock = 10
        items.add(item)

        item = Item("Strawberry Macchiato", 100.0f,R.drawable.strawberry_macchiato)
        item.itemId = 34
        item.description = "Creamy milky white fresh milk added with fresh strawberries and strong espresso."
        item.category = "Specials"
        item.itemStock = 10
        items.add(item)
    }
}