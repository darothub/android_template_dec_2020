package com.peacedude.lassod_tailor_app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.peacedude.lassod_tailor_app.R
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //
    }

    override fun onResume() {
        super.onResume()
    }
}

//fun main(){
//    val res = climbingLeaderboard(arrayOf(100, 90, 90, 80, 75, 60), arrayOf(50, 65, 77, 90, 102))
//    println(res.toList())
//}
//
//// Complete the climbingLeaderboard function below.
//fun climbingLeaderboard(scores: Array<Int>, alice: Array<Int>): Array<Int> {
//    var newArr = scores.toMutableList()
//    var rank = ArrayList<Int>()
//    alice.map { it ->
//        newArr.add(it)
//        val hashy = newArr.groupBy {element -> element}
//        val sorta = hashy.keys.sorted().reversed()
//        rank.add(sorta.indexOf(it) + 1)
//    }
////    for(i in 0 until alice.size){
////       newArr.add(alice.get(i))
////        val hashy = newArr.groupBy {
////            it
////        }
////        val sorta = hashy.keys.sorted().reversed()
////        rank.add(sorta.indexOf(alice.get(i)) + 1)
////    }
//
//    return rank.toTypedArray()
//
//}