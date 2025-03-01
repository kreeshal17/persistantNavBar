package com.krishal.persistancebottombar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.toObject
import com.krishal.persistancebottombar.MOdel.PostModel
import com.krishal.persistancebottombar.MOdel.UserModel

 class AuthViewModel : ViewModel() {
    private val _searchedUser = MutableLiveData<UserModel?>()
    val searchedUser: MutableLiveData<UserModel?> = _searchedUser
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
     private val _userpost =MutableLiveData<List<PostModel?>>()
     val userPost : LiveData<List<PostModel?>> =_userpost



     private val _searchesuserPost = MutableLiveData<List<PostModel>>()
     val searcheduserPost :LiveData<List<PostModel>> =_searchesuserPost

     private val _postStatus = MutableLiveData<String?>()
     val postStatus: LiveData<String?> = _postStatus



    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val _userData = MutableLiveData<UserModel?>()
    val userData: LiveData<UserModel?> = _userData
    fun isUserAuthenticated():Boolean
    {
        return FirebaseAuth.getInstance().currentUser!=null
    }

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _firebaseUser.value = firebaseAuth.currentUser
            firebaseAuth.currentUser?.uid?.let { fetchUserData(it) }
        }
    }

    // Fetch the latest user data from Firestore
    fun fetchUserData(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(UserModel::class.java)
                    _userData.value = user
                }
            }
            .addOnFailureListener {
                _userData.value = null
            }
    }

    // Login Function
    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    fetchUserData(userId) // Fetch latest user data
                    onResult(true, "Login successful")
                }
            } else {
                onResult(false, "Something went wrong")
            }
        }
    }


    // Signup Function
    fun signUp(email: String, password: String, name: String, bio: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = task.result?.user?.uid
                if (userId == null) {
                    onResult(false, "User ID is null")
                    return@addOnCompleteListener
                }

                val userModel = UserModel(email, password, name, bio)
                db.collection("users").document(userId).set(userModel).addOnCompleteListener { storeTask ->
                    if (storeTask.isSuccessful) {
                        fetchUserData(userId) // Fetch the newly added user data
                        onResult(true, "Successfully added")
                    } else {
                        onResult(false, "Something went wrong")
                    }
                }
            } else {
                onResult(false, "Signup failed")
            }
        }
    }

    // Forget Password Function
    fun forgetPassword(email: String, onResult: (Boolean, String) -> Unit) {
        if (email.isEmpty()) {
            onResult(false, "Enter the email")
            return
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, "Recovery email sent successfully")
            } else {
                onResult(false, task.exception?.message ?: "Something went wrong")
            }
        }
    }

    // Logout Function
    fun logout(onDone: () -> Unit) {
        auth.signOut()
        _firebaseUser.value = null
        _userData.value = null
        onDone()
    }



    fun fetchUserFromName(name: String)
    {
   if(name.isEmpty())
   {
       _searchedUser.value=null
       return
   }
        db.collection("users").whereEqualTo("name",name).get().addOnCompleteListener { task->

            if(task.isSuccessful)
            {
                val document =task.result?.documents
                if(!document.isNullOrEmpty())
                {
                    val user= document[0].toObject(UserModel::class.java)
                    _searchedUser.value=user
                }
                else{
                    _searchedUser.value=null

                }
            }
            else{
                _searchedUser.value=null
            }
        }
    }
     fun fetchpostAByUserName(name: String) {
         if (name.isEmpty()) {
             _searchesuserPost.value = emptyList()
             return
         }

         db.collection("posts")
             .whereEqualTo("name", name)
//             .orderBy("timeStamp", Query.Direction.DESCENDING)
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     val documents = task.result?.documents
                     if (!documents.isNullOrEmpty()) {
                         val posts = documents.mapNotNull { it.toObject(PostModel::class.java) }
                         _searchesuserPost.value = posts
                     } else {
                         _searchesuserPost.value = emptyList()
                     }
                 } else {
                     _searchesuserPost.value = emptyList()
                 }
             }
     }



     //function to add postId
      fun adPost(name:String,content:String,) {
     val userId = auth.currentUser?.uid!!

     val postId = db.collection("posts").document().id
     val post = PostModel(postId, userId, name, content)
     db.collection("posts").document(postId).set(post).addOnSuccessListener {
         _postStatus.value = "added succesfully"


     }.addOnFailureListener {


         _postStatus.value = "failed exception"


     }

 }

     //fetcvh all post of user
     fun fetchAllPost()
     {
         val userId =auth.currentUser?.uid!!
         if(userId.isEmpty())
         {
             _userpost.value= emptyList()
             return
         }
         db.collection("posts").whereEqualTo("userId",userId).orderBy("timeStamp",com.google.firebase.firestore.Query.Direction.DESCENDING).get().addOnCompleteListener{

             task->
             if(task.isSuccessful)
             {
               val document=  task.result?.documents
                 if(!document.isNullOrEmpty())
                 {
                     val post = document.mapNotNull { it.toObject(PostModel::class.java) }
                        _userpost.value =post


                 }
                 else{
                     _userpost.value= emptyList()

                 }


             }
             else{
                 _userpost.value= emptyList()
             }


         }


     }


//     fun fetchpostAByUserName(name:String)
//     {
//         if(name.isEmpty())
//         {
//             _searchesuserPost.value= emptyList()
//             return
//         }
//
//         db.collection("posts").whereEqualTo("name",name).orderBy("timeStamp",com.google.firebase.firestore.Query.Direction.DESCENDING).get().addOnCompleteListener {
//             task->
//             if (task.isSuccessful)
//             {
//                 val document=task.result?.documents
//                 if(!document.isNullOrEmpty())
//                 {
//                     val post =document.mapNotNull { it.toObject(PostModel::class.java) }
//                     _searchesuserPost.value
//                 }
//                 else{
//                     _searchesuserPost.value= emptyList()
//                 }
//
//
//             }
//             else{
//                 _searchesuserPost.value= emptyList()
//             }
//
//
//         }




 }









