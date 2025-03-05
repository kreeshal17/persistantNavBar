package com.krishal.persistancebottombar.Screen





import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.krishal.persistancebottombar.AuthViewModel
import com.krishal.persistancebottombar.constant
import org.jetbrains.annotations.Async

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun signUp(navController: NavController,authViewModel: AuthViewModel)

{


    var  selectedImage by remember {
        mutableStateOf<Uri?>(Uri.parse("https://wallpapers.com/images/featured/cool-profile-picture-87h46gcobjl5e4xu.jpg"))
    }
var isLoading by remember {
    mutableStateOf(false   )
}

    var show by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var eye by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {selectedImage=it}
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                )
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth().verticalScroll(rememberScrollState())
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        )
        {


            AnimatedVisibility(
                visible = show,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
                exit = fadeOut(animationSpec = tween(durationMillis = 5000))
            ) {
                Text(
                    text = "Sign Up Details",
                    style = TextStyle(
                        color = Color.Black,
                        fontStyle = FontStyle.Normal,
                        fontSize = 22.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
            AsyncImage(model = selectedImage,
                contentDescription = null, modifier = Modifier.clip(CircleShape).size(
                    160.dp,
                    200.dp
                ), contentScale = ContentScale.Crop)
             Button(onClick = {
                 imagePickerLauncher.launch(
                     PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                 )

             }) {
                 Text(text="Upload your image")
             }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = "Enter your name") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text(text = "Add bio to your Profile") },
                modifier = Modifier.fillMaxWidth(0.9f),


            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Enter your email address") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Enter your password") },
                visualTransformation = if (eye) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { eye = !eye }) {
                        Icon(
                            imageVector = if (eye) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(30.dp))


            Button(
                onClick = {
                    isLoading=true
                   authViewModel.signUp(email,password,name,bio){
                       success,b->
                       if(success)
                       {
                           isLoading=false
                           navController.navigate(constant.home)
                           {
                               popUpTo(constant.signUp)
                               {
                                   inclusive=true
                               }
                           }

                       }
                       else{
                           isLoading=false
                           Toast.makeText(context,"failure",Toast.LENGTH_SHORT).show()
                       }
                   }
                },
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027)),
                modifier = Modifier.fillMaxWidth(0.9f).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (!isLoading)"Signup" else "creating Account", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextButton(onClick = { navController.navigate(constant.login) }) {
                Text(text = "Already have an account? Log-In", color = Color(0xFF2C5364))
            }
        }
    }
}