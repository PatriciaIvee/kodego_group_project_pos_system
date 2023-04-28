package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.receipts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentReceiptsBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.OrderList

class ReceiptsFragment : Fragment() {

    private var _binding: FragmentReceiptsBinding? = null
    private val binding get() = _binding!!
    private var order: Order = Order()
    private var orders: OrderList = OrderList()
    private var db = Firebase.firestore
    private var ordersList: ArrayList<OrderList> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Composable UI container
        val receiptsUiContainer = binding.receiptsUiContainer

        val receiptsView = ComposeView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        receiptsUiContainer.addView(receiptsView)

        // Render the ReceiptsScreen composable UI inside the receiptsView


        db = FirebaseFirestore.getInstance()

        db.collection("order").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents){
                    val order: OrderList? = data.toObject(OrderList::class.java)
                    if (order != null) {
                        ordersList.add(order)
                    }
                }
                orders = ordersList[5]
//                for (ordersOrders in ordersList) {
//                    orders
//                }

                receiptsView.setContent {
                    ReceiptScreen()
                }

            } else {
            }
        }
            .addOnFailureListener{
                Toast.makeText(activity, "Order collection failed.", Toast.LENGTH_LONG).show()
            }
    }

    @Composable
    fun ReceiptScreen() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Receipt",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(orders.orderList.size) {
                    ReceiptItemRow()
                    Divider(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }

    @Composable
    fun ReceiptItemRow() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "OrderID\n${orders.orderList[0].orderId}",
                fontSize = 18.sp)
            Text(
                text = "₱${orders.orderList[0].itemPrice * orders.orderList[0].orderQuantity}",
                fontSize = 20.sp)
        }
    }

//    @Preview
//    @Composable
//    fun ReceiptScreenPreview() {
//        ReceiptScreen()
//    }

}