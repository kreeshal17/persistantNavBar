//package com.krishal.persistancebottombar.utils
//
//import android.content.Context
//import android.content.Context.MODE_PRIVATE
//import android.content.SharedPreferences
//import android.view.Display.Mode
//
//object SharedPRep {
//
//
//    fun storeData(name:String, email:String,bio:String,selectedImage:String,context:Context)
//    {
//   val sharedPreferences = context.getSharedPreferences("users", MODE_PRIVATE)
//
//        val editor= sharedPreferences.edit()
//        editor.putString("name",name)
//        editor.putString("email",email)
//        editor.putString("bio",bio  )
//        editor.putString("selectedImage",selectedImage)
//        editor.apply()
//    }
//
//
//    fun getName(context: Context):String{
//        val sharedPreferences =context.getSharedPreferences("users", MODE_PRIVATE)
//        return sharedPreferences.getString("name","")!!
//
//    }
//    fun getEmail(context: Context):String
//    {
//        val sharedPreferences=context.getSharedPreferences("users", MODE_PRIVATE)
//        return sharedPreferences.getString("email","")!!
//    }
//    fun getBio(context: Context):String
//    {
//        val sharedPreferences=context.getSharedPreferences("users", MODE_PRIVATE)
//        return sharedPreferences.getString("bio","")!!
//    }
//    fun getImage(context: Context):String{
//        val sharedPreferences=context.getSharedPreferences("users", MODE_PRIVATE)
//        return sharedPreferences.getString("selectedImage","")!!
//    }
//
//}