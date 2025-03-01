package com.krishal.persistancebottombar.Screen

import android.net.Uri
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.krishal.persistancebottombar.AuthViewModel
import com.krishal.persistancebottombar.constant

@Composable
fun SettingScreen(authViewModel: AuthViewModel,navController: NavController) {
    val user by authViewModel.userData.observeAsState()
    val selectedImage by remember {
        mutableStateOf<Uri?>(Uri.parse("https://wallpapers.com/images/featured/cool-profile-picture-87h46gcobjl5e4xu.jpg"))
    }

    // Gradient Background
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A11CB), Color(0xFF2575FC)),
        startY = 0f,
        endY = 800f
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Profile Image with Edit Button
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .shadow(16.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
            ) {
                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Edit Icon (Floating Action Button)
                FloatingActionButton(
                    onClick = { /* Handle image upload */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Picture"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Welcome Message
            Text(
                text = if (user != null) "Hey ${user!!.name}!" else "Hey there!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Welcome to Your App",
                fontSize = 20.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(24.dp))
   user?.let {
        Text(text="Your current Email is ${it.email}")
   }
            Spacer(modifier = Modifier.height(20.dp))
            // User Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    user?.let {


                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = " ${it.bio}",
                            fontSize = 16.sp,
                            color = Color.Black.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    } ?: Text(
                        text = "Loading user data...",
                        fontSize = 16.sp,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }
            Button(onClick = {authViewModel.logout {
                navController.navigate(constant.login)

            } }) {
                Text("Log Out")
            }
        }
    }
}