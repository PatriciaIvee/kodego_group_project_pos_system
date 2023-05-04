package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentSalesBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderList
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.SalesSummary

class SalesFragment : Fragment() {
    private lateinit var binding: FragmentSalesBinding
    private var db = Firebase.firestore
    private var ordersList: ArrayList<OrderList> = ArrayList()
    private var orderList = ArrayList<Order>()
    private var numberOfSales = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Composable UI container
        val salesUiContainer = binding.salesUiContainer

        val salesView = ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        salesUiContainer.addView(salesView)

        db = FirebaseFirestore.getInstance()

        db.collection("order").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents){
                    val order: OrderList? = data.toObject(OrderList::class.java)
                    if (order != null) {
                        ordersList.add(order)
                    }
                }
                for (ordersOrders in ordersList) {
                    numberOfSales ++
                    for (ordersOrdersOrders in ordersOrders.orderList) {
                        orderList.add(ordersOrdersOrders)
                    }
                }

                salesView.setContent {
                    SalesSummaryScreen()
                }


            } else {
            }
        }
            .addOnFailureListener{
                Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
            }
    }

    @Composable
    fun SalesSummaryScreen() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Total Sales: ₱${getSalesSummary(orderList).totalSales}",
                style = MaterialTheme.typography.h4
            )
            Toast.makeText(requireContext().applicationContext, "${orderList.size}", Toast.LENGTH_SHORT).show()
            Text(
                text = "Number of Sales: ${getSalesSummary(orderList).numSales}",
                style = MaterialTheme.typography.h4
            )

            Text(
                text = "Average Sale: ₱${getSalesSummary(orderList).avgSale}",
                style = MaterialTheme.typography.h4
            )
        }
    }

    private fun getSalesSummary(orders: List<Order>): SalesSummary {

        var totalSales = orders.sumOf { it.itemPrice.toDouble() }
        var numSales = numberOfSales
        var avgSale = if (numSales > 0) totalSales / numSales else 0.0

        return SalesSummary(totalSales, numSales, avgSale)
    }
}
