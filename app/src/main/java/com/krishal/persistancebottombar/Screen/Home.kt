package com.krishal.persistancebottombar

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.krishal.persistancebottombar.MOdel.PostModel

@Composable
fun homeScreen(authViewModel: AuthViewModel) {

val context= LocalContext.current

        var post by remember { mutableStateOf("") }
        val user by authViewModel.userData.observeAsState()

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(20.dp))

            // Post Input
            OutlinedTextField(
                value = post,
                onValueChange = { post = it },
                label = { Text("What's on your mind?") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Post Button
            Button(
                onClick = {
                    val userName = user?.name ?: "Anonymous"
                    if (post.isNotEmpty()) {
                        authViewModel.adPost(userName, post)
                        Toast.makeText(context,"sucessfully added", Toast.LENGTH_SHORT).show()
                        post = "" // Clear the input field after posting
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Post")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Post")
            }
        }
    }


