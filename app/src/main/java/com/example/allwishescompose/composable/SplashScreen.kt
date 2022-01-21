package com.example.allwishescompose.composable

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.allwishescompose.R
import com.example.allwishescompose.ui.theme.Purple700
import com.example.allwishescompose.utils.NavigationDestinations
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {

    val scale = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    LaunchedEffect(key1 = true){
        scale.animateTo( targetValue = 0.7f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(1500)
        navController.navigate(NavigationDestinations.mainScreen){
            popUpTo(NavigationDestinations.splashScreen){
                inclusive = true
            }
        }
    }
    Image(painter = painterResource(id = R.drawable.bg),
        alignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        contentDescription = "Main BG Image")

    Box(modifier = Modifier
        .padding()
        .fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.name),
            alignment = Alignment.Center,
            contentDescription = "Center Image")
    }



}