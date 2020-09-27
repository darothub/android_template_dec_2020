package com.peacedude.lassod_tailor_app.model.request

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

class Client (val name:String, val phone:String, val email:String, val shippingAddress:String):Serializable,
    ParentData {
    val id:String?=""
    val tailorId:String?=""
    val gender:String=""
    val country:String?= ""
}