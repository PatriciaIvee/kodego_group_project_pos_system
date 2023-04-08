package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model.Order

class OrderListViewModel: ViewModel() {
    var totalOrderQuantity = MutableLiveData<Int>(0)

    // Keep track of the list of orders
    private val _orders = MutableLiveData<List<Order>?>()
    val orders: MutableLiveData<List<Order>?>
        get() = _orders

    init {
        _orders.value = mutableListOf()
    }

    fun addOrder(order: Order) {
        val orders = _orders.value?.toMutableList()
        orders?.add(order)
        _orders.value = orders
        // Update the total order quantity
        totalOrderQuantity.value = getTotalOrderQuantity(orders ?: emptyList())
    }

    private fun getTotalOrderQuantity(orders: List<Order>): Int {
        var totalQuantity = 0
        for (order in orders) {
            totalQuantity += order.orderQuantity
        }
        return totalQuantity
    }
}