package com.peacedude.lassod_tailor_app.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import dagger.android.support.DaggerAppCompatActivity
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.LocalTime

abstract class BaseActivity : DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //
    }

    override fun onResume() {
        super.onResume()
    }
}
//
//@RequiresApi(Build.VERSION_CODES.O)
//fun main(){
//    val res = formingMagicSquare(arrayOf(arrayOf(5, 3, 4), arrayOf(1, 5, 8), arrayOf(6, 4, 2)))
////    staircase(4)
//    println(res)
//}
//
//// Complete the climbingLeaderboard function below.
//fun climbingLeaderboard(scores: Array<Int>, alice: Array<Int>): Array<Int> {
//
//    return Array(scores.size){
//        var a:Array<Int>
//        return alice.map {
//            a = scores + it
//            val res = a.toSortedSet().reversed()
//            res.indexOf(it)+1
//
//        }.toTypedArray()
//    }
//
//}
//@SuppressLint("SimpleDateFormat")
//@RequiresApi(Build.VERSION_CODES.O)
//fun timeConversion(s: String): String {
//    val g = SimpleDateFormat("hh:mm:ss aa")
//    val o = SimpleDateFormat("HH:mm:ss")
//    val pmOrAm = s.substring(s.length-2, s.length)
//    val d = g.parse(s.substring(0 until s.length-2)+" "+pmOrAm)
//    val op = o.format(d)
//    return op
//}
//
//fun staircase(n: Int): Unit {
//
//    for(i in n downTo 1){
//        println(" ".repeat(i-1) + "#".repeat(n-i+1))
//    }
//}
//
//// Complete the formingMagicSquare function below.
//fun formingMagicSquare(s: Array<Array<Int>>): Int {
//   val v1 = arrayOf(s[0][0], s[1][0], s[2][0])
//   val v2 = arrayOf(s[0][1], s[1][1], s[2][1])
//   val v3 = arrayOf(s[0][2], s[1][2], s[2][2])
//   val d1 = arrayOf(s[0][0], s[1][1], s[2][2])
//   val d2 = arrayOf(s[0][2], s[1][1], s[2][0])
//   val u =s.map {
//       15 - it.sum()
//   }
//    val per00 = u[0] + s[0][0] + s[0][1] + s[0][2] == u[0] + v1[0] + v1[1] + v1[2]
//    val per01 =  s[0][0] + (u[0] + s[0][1]) + s[0][2] == u[0] + v2[0] + v2[1] + v2[2]
//    val per02 =  s[0][0] +  s[0][1] + (u[0] + s[0][2]) == u[0] + v3[0] + v3[1] + v3[2]
//    val d00sum = u[0] + d1[0] + d1[1] + d1[2] == u[0] + s[0][0] + s[0][1] + s[0][2]
//    val d01sum = u[0] + d1[0] + d1[1] + d1[2] == u[0] + s[0][0] + s[0][1] + s[0][2]
//    println(d00sum)
//    return 0
//}