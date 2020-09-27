package com.peacedude.lassod_tailor_app.model.request

import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

class Measurement (val name:String, val value:String):Serializable, ParentData {}