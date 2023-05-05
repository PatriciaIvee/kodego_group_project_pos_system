package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model

import android.util.Log
import com.google.firebase.firestore.ServerTimestamp
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

open class Item (var itemName: String = "", var itemPrice: Float = 0.0f,var img:Int ){
    var itemId: Int = 0
    var description: String = ""
    var category:String = ""
    var itemType: String = ""
    var itemStock: Int = 0

    constructor(): this("",0.0f, R.drawable.ic_baseline_image_24)
}

class Order(itemName: String = "",itemPrice: Float = 0.0f,img:Int): Item (itemName, itemPrice, img){


    var orderId: String? = null
    //ORDER_TOTAL IS ALWAYS INITIALIZED TO 0 HERE
    //ORDER_TOTAL NEEDS TO BE UPDATED WHEN ORDER QUANTITY IS UPDATED

    var orderQuantity: Int = 0
        set(value) {
            field = value
            orderTotal = if (value == 0) 0.0f else value * itemPrice
        }
    var orderTotal: Float = 0.0f

    //    var orderTotal = orderQuantity * itemPrice
    var totalOrderQuantity :Int = 0


    var datePurchased: String? = null // Add this property to track the purchase date


    constructor() : this("",0.0f, R.drawable.ic_baseline_image_24)
}