package com.krishal.persistancebottombar.Screen


import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.krishal.persistancebottombar.AuthViewModel


import com.krishal.persistancebottombar.constant

@Composable
fun loginScreen(navController: NavController,authViewModel: AuthViewModel) {





var isloading by remember {
 mutableStateOf(false)
}

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Text(
                "Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    if(email.isNotEmpty()&&password.isNotEmpty())
                    {
                    authViewModel.login(email, password)
                    { sucess, failure ->
                        if (sucess) {
                            isloading = false
                            navController.navigate(constant.home) {
                                popUpTo(constant.login)
                                {
                                    inclusive = true
                                }
                            }
                        } else {
                            isloading = false
                            Toast.makeText(context, "Error occurs", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }


                }else{
                        Toast.makeText(context, "Error occurs", Toast.LENGTH_SHORT)
                            .show()

                    }                },
                enabled = !isloading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027))
            ) {
                Text("Login", fontSize = 18.sp, color = Color.White)
            }


            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = {
                if(email.isNotEmpty())
                {
                authViewModel.forgetPassword(email)
                {
                    success,failure->
                    if(success)
                    {
                        Toast.makeText(context,"sucessfully sent an email of recovery",Toast.LENGTH_SHORT).show()

                    }
                    else {
                        Toast.makeText(context, "Error occur", Toast.LENGTH_SHORT).show()
                    }
                    }
                }
                else{
                    Toast.makeText(context, "Error occur", Toast.LENGTH_SHORT).show()

                }

            }) {
                Text("Forgot password?", color = Color(0xFF203A43))
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.navigate(constant.signUp) }) {
                Text("Don't have an account? Sign Up", color = Color(0xFF2C5364))
            }

            message?.let {
                Text(it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }
        }
        }
    }

