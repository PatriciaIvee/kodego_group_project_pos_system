package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.items


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.adapter.ItemCategoryAdapter
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentItemsBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Category
import java.util.*

class ItemsFragment : Fragment() {

    //BETTER IF WE HAVE AN API FOR THE ITEMS AND THE CATEGORIES

    private var _binding: FragmentItemsBinding? = null
    private val binding get() = _binding!!

    private lateinit var itemCategoryAdapter: ItemCategoryAdapter
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private var categories: ArrayList<Category> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCategory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentItemsBinding.inflate(inflater, container, false)


        // Show bottom sheet dialog
        itemCategoryAdapter = ItemCategoryAdapter(categories,requireActivity())
        binding.recyclerItemCategory.adapter = itemCategoryAdapter
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.getWindow()?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        return binding.root
    }

    fun initCategory(){
        categories.add(Category("Coffee", R.drawable.ic_baseline_image_24))
        categories.add(Category("Non-Coffee", R.drawable.ic_baseline_image_24))
        categories.add(Category("Extras", R.drawable.ic_baseline_image_24))
        categories.add(Category("Pastries", R.drawable.ic_baseline_image_24))
        categories.add(Category("Sandwiches", R.drawable.ic_baseline_image_24))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize bottom sheet dialog
        bottomSheetDialog = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_item_fragment, null)
        bottomSheetDialog.setContentView(bottomSheetView)

        // Show bottom sheet dialog
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}