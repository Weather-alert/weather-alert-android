package com.example.weatheralert.presentation.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.example.weatheralert.R
import com.example.weatheralert.api.NetworkModule
import com.example.weatheralert.presentation.MainViewModel
import com.example.weatheralert.configs.AppConfig
import com.example.weatheralert.ui.theme.WeatherAlertTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("MissingPermission")
@Composable
fun MainScreen(vm: MainViewModel){
    WeatherAlertTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                val isLoading = remember { mutableStateOf(false) }

                Button(
                    {
                        CoroutineScope(Dispatchers.IO).launch {
                            launch(Dispatchers.Main) {
                                isLoading.value = true
                            }
                            val r = NetworkModule.userService.createUser(id = AppConfig.androidId)
                            launch(Dispatchers.Main) {
                                isLoading.value = false
                            }
                            if(r.isSuccessful()){
                                Timber.d("Successfully created user")
                            } else{
                                Timber.e("Failed to create user")
                            }
                        }
                    }
                ) {
                    Text("Click me to register a user")
                    if (isLoading.value) {
                        GifImage()
                    }
                }
            }
        }
    }
}

@Composable
fun GifImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(20.dp),   //crops the image to circle shape
        painter = rememberDrawablePainter(
            drawable = getDrawable(
                LocalContext.current,
                R.drawable.loading
            )
        ),
        contentDescription = "Loading animation",
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
