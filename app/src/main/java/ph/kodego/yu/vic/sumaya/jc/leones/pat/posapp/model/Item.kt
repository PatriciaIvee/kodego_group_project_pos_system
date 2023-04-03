package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model

open class Item {
    var itemName: String = ""
    var itemType: String = ""
    var itemPrice: Float = 0.0f
    var itemStock: Int = 0
}

class Order: Item(){
    var orderQuantity: Int = 0

}