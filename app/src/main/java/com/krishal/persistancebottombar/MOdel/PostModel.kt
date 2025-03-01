package com.krishal.persistancebottombar.MOdel

data class PostModel(
    val PostId:String="" ,
    val UserId:String="",
    val name :String= "",
    val content:String= "",
val timeStamp:Long= System.currentTimeMillis()


)
{
    constructor() : this("", "", "", "", 0) // Firestore needs this empty constructor
}
