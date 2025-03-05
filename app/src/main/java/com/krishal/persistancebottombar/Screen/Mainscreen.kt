package com.krishal.persistancebottombar.Screen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.krishal.persistancebottombar.AuthViewModel
import com.krishal.persistancebottombar.R
import com.krishal.persistancebottombar.constant
import com.krishal.persistancebottombar.homeScreen
import kotlinx.coroutines.delay


@Composable
  fun MainScreenn(authViewModel: AuthViewModel)
  {

      var navController = rememberNavController()
      Scaffold(
          bottomBar =
          {
              val currentRoute =
                  navController.currentBackStackEntryAsState().value?.destination?.route
              if (currentRoute != constant.splash&&currentRoute !=constant.login &&currentRoute != constant.signUp) {

                  BottomAppBar(navController)

              }
          }

      ) {


          innerPadding->
              NavGraphScreen(navController,modifier= Modifier.padding(innerPadding),authViewModel)

      }


  }

@Composable
fun NavGraphScreen(navController:NavHostController, modifier: Modifier,authViewModel: AuthViewModel) {

    NavHost(navController=navController, startDestination = constant.splash,modifier=modifier)
    {
        composable(constant.splash){ splashScreen(navController,authViewModel)}
        composable(constant.home){ homeScreen(authViewModel)}
        composable(constant.setting){ SettingScreen(authViewModel,navController)}
            composable(constant.notification){


                notification(authViewModel,navController)}
        composable(constant.login){ loginScreen(navController,authViewModel)}
        composable(constant.signUp){signUp(navController,authViewModel)}
        composable(constant.friendlist){ friendList(authViewModel)}
        composable(constant.profileedit){ profileedit(navController,authViewModel)}



//        composable("${constant.userProf}/{search}") { backStackEntry ->
//            val search = backStackEntry.arguments?.getString("search") ?: ""
//            userProfile(authViewModel, search)

        }
    }









@Composable
fun BottomAppBar(navController: NavController) {
    var items = listOf(

        bottombarItems("Home", Icons.Default.Home,constant.home),

    bottombarItems("Search", Icons.Default.Search,constant.notification),
        bottombarItems("profile", Icons.Default.Person,constant.setting),

        )
    var selectedItem by remember{
        mutableStateOf(0)
    }

    NavigationBar {
        items.forEachIndexed { index,item ->
            NavigationBarItem(
                selected = selectedItem==index,//return true if the sealecteditem index i.e initially zero is equall to the index of that presenmt clicked item
                onClick = {
                    selectedItem=index

                        navController.navigate(item.route) {
                            popUpTo(constant.home) {
                                saveState = true

                            }
                            launchSingleTop = true
                            restoreState = true
                        }


                },
                icon = {
                    Icon(imageVector =item.icon , contentDescription = null)
                },

                label ={
                    Text(text=item.label)
                }

            )





        }





    }



}



data class bottombarItems(
    val label:String,
    val icon:ImageVector,
    val route:String
)

