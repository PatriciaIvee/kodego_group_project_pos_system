package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model

import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R

open class Item (var itemName: String = "", var itemPrice: Float = 0.0f,var img:Int ){
    var itemId: Int = 0
    var description: String = ""
    var category:String = ""
    var itemType: String = ""
    var itemStock: Int = 0

    constructor(): this("",0.0f, R.drawable.ic_baseline_image_24)
}

class Order(itemName: String = "",itemPrice: Float = 0.0f,img:Int): Item (itemName, itemPrice, img){


    var orderId: Int = 0
    //ORDER_TOTAL IS ALWAYS INITIALIZED TO 0 HERE
    //ORDER_TOTAL NEEDS TO BE UPDATED WHEN ORDER QUANTITY IS UPDATED
    var orderQuantity: Int = 0
        set(value) {
            field = value
            orderTotal = value * itemPrice
            totalOrderQuantity += value
        }
    var orderTotal = orderQuantity * itemPrice
    var totalOrderQuantity :Int = 0

    constructor() : this("",0.0f, R.drawable.ic_baseline_image_24)
}