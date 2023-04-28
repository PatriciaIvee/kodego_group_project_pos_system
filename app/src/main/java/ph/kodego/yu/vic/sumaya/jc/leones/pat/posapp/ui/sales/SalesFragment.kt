package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.sales

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.FragmentSalesBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.SalesSummary

class SalesFragment : Fragment() {
    private lateinit var binding: FragmentSalesBinding

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

        // Render the SalesSummaryScreen composable UI inside the salesUI
        salesView.setContent {
            SalesSummaryScreen()
        }
    }

    @Composable
    fun SalesSummaryScreen() {

        val orderList = listOf(
            Order("item 1", 1.0f, R.drawable.ic_baseline_image_24),
            Order("item 2", 3.0f, R.drawable.ic_baseline_image_24))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Total Sales: ₱${getSalesSummary(orderList).totalSales}",
                style = MaterialTheme.typography.h4
            )

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

    fun getSalesSummary(orders: List<Order>): SalesSummary {

        val totalSales = orders.sumOf { it.itemPrice.toDouble() }
        val numSales = orders.size
        val avgSale = if (numSales > 0) totalSales / numSales else 0.0

        return SalesSummary(totalSales, numSales, avgSale)
    }
}
