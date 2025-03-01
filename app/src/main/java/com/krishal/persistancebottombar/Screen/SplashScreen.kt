package com.krishal.persistancebottombar.Screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.krishal.persistancebottombar.AuthViewModel
import com.krishal.persistancebottombar.R
import com.krishal.persistancebottombar.constant
import kotlinx.coroutines.delay


@Composable
fun splashScreen(navController: NavController,authViewModel: AuthViewModel)
{
    val isLoggedIn = remember { mutableStateOf<Boolean?>(null) }
    var scale= remember{
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 5000,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                })
        )
        delay(200)
        isLoggedIn.value = authViewModel.isUserAuthenticated()
        if(isLoggedIn.value==false) {
            navController.navigate(constant.login) {
                popUpTo(constant.splash) {
                    inclusive = true
                }
            }
        }
            else{
                navController.navigate(constant.home)
                {
                    popUpTo(constant.home){
                        inclusive=true
                    }
                }

        }
        }



    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        Image(painter = painterResource(R.drawable.img), contentDescription = null, modifier = Modifier.scale(scale.value))
    }

}