package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import kotlin.math.pow
import kotlin.math.roundToInt

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
        val salesSummary = getSalesSummary(orderList)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.LightGray, CircleShape)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sales2),
                            contentDescription = null,
                            tint = Color(R.color.light_green),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total Sales",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₱${salesSummary.totalSales}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.LightGray, CircleShape)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.receipts),
                            contentDescription = null,
                            tint = Color(R.color.light_green),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Number of Sales",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${salesSummary.numSales}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color.LightGray, CircleShape)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.graph),
                            contentDescription = null,
                            tint = Color(R.color.light_green),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Average Sale",
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "₱${salesSummary.avgSale}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }


    private fun getSalesSummary(orders: List<Order>): SalesSummary {

        var totalSales = orders.sumOf { it.itemPrice.toDouble() }
        var numSales = numberOfSales
        var avgSale = if (numSales > 0) (totalSales / numSales)
            .roundToDecimalPlaces(2) else 0.0

        return SalesSummary(totalSales, numSales, avgSale)
    }

    private fun Double.roundToDecimalPlaces(decimalPlaces: Int): Double {
        val factor = 10.0.pow(decimalPlaces.toDouble())
        return (this * factor).roundToInt() / factor
    }
}
