package com.krishal.persistancebottombar.Screen

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.krishal.persistancebottombar.AuthViewModel
import com.krishal.persistancebottombar.constant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(authViewModel: AuthViewModel, navController: NavController) {
    val user by authViewModel.userData.observeAsState()
    val userpost by authViewModel.userPost.observeAsState(emptyList())
    val selectedImage by remember { mutableStateOf<Uri?>(Uri.parse("https://wallpapers.com/images/featured/cool-profile-picture-87h46gcobjl5e4xu.jpg")) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)),
        startY = 0f,
        endY = 1000f
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logout { navController.navigate(constant.login) }
                    }) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(constant.friendlist)

                    }) {
                        Icon(imageVector =Icons.Default.People, contentDescription = null)

                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF2C5364))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .shadow(8.dp)
                        .background(Color.White)
                ) {
                    AsyncImage(
                        model = selectedImage,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = user?.name ?: "Hey there!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Welcome to your profile", fontSize = 18.sp, color = Color.White.copy(alpha = 0.8f))
                Spacer(modifier = Modifier.height(16.dp))
                user?.let {
                    Text(text = "Email: ${it.email}", fontSize = 16.sp, color = Color.White.copy(alpha = 0.8f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = {navController.navigate(constant.profileedit) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        user?.let {
                            Text(text = it.bio, fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (userpost.isEmpty()) {
                            Text("No posts found.", fontSize = 16.sp, color = Color.LightGray)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(userpost) { post ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f))
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            if (post != null) {
                                                Text(text = "Title: ${post.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                            if (post != null) {
                                                Text(text = "Content: ${post.content}", fontSize = 16.sp, color = Color.White.copy(alpha = 0.9f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { authViewModel.fetchAllPost() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5), contentColor = Color.White)
                ) {
                    Text("Show Your Posts")
                }
            }
        }
    }
}


@Composable
fun profileedit(navController: NavController,authViewModel: AuthViewModel)
{
     var context= LocalContext.current
    var bio by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(value =bio, onValueChange = { bio=it }, label = {
            Text(text = "enter your new bio")
        })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value =name, onValueChange = { name =it }, label = {
            Text(text = "enter your new name ")
        })
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            authViewModel.updateProfile(bio,name)
            {
                success,failure->
                if(success)
                {
                   Toast.makeText(context,"successfullly updated",Toast.LENGTH_SHORT).show()
                    navController.navigate(constant.setting)
                    {
                        popUpTo(constant.profileedit)
                        {
                            inclusive=false
                        }
                    }
                }
                else{
                    Toast.makeText(context,"error try later",Toast.LENGTH_SHORT).show()
                }
            }

        }) {
            Text(text="update")
        }

    }


}
