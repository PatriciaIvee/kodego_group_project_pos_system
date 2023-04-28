package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model

import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.R

//NOT SURE IF THERE'S A BETTER WAY TO THIS
class Category(var categoryName: String, var img: Int) {
    var categoryId : Int = 0
    var description : String = ""


    constructor(): this("", R.drawable.ic_baseline_image_24)

}
