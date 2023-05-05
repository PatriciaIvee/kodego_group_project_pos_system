package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.model

import com.google.type.DateTime
import java.time.LocalDateTime
import java.util.*

data class OrderHistory(var uid: String? = null,
                        var datePurchased: String? = null,
                        var served: Boolean = false)