package com.peacedude.lassod_tailor_app.model.request

import java.io.Serializable

class ResourcesVideo(val videoUri:String, val videoTitle:String, val videoMins:String):Serializable
class ResourcesArticlePublication(val articleImageUri:String, val articleTitle:String, val articleAuthor:String):Serializable