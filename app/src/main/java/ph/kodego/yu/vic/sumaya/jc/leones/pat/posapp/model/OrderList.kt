package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model

import java.util.*
import kotlin.collections.ArrayList

data class OrderList(var uid: String? = null,
                     val orderList: ArrayList<Order> = ArrayList(),
                     var datePurchased: String? = null,){

}

