package com.peacedude.lassod_tailor_app.model.response

import com.google.gson.annotations.SerializedName
import com.peacedude.lassod_tailor_app.model.parent.ParentData
import java.io.Serializable

data class ArticleList (
    val article: List<Article>
):Serializable, ParentData

data class Article (
    val id: String,
    @SerializedName("artisanId")
    val tailorID: String,
    val title: String,
    val description: String,
    val isBlacklisted: Boolean = false,
    val body: String?=null,
    val articleImage: String?=null,
    val createdAt: String,
    val updatedAt: String
):Serializable, ParentData