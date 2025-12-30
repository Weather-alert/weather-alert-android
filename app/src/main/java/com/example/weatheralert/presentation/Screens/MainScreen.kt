package com.example.weatheralert.presentation.Screens

import android.annotation.SuppressLint
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.example.weatheralert.R
import com.example.weatheralert.ViewModel
import com.example.weatheralert.api.NetworkModule
import com.example.weatheralert.presentation.MainViewModel
import com.example.weatheralert.repository.AppConfig
import com.example.weatheralert.ui.theme.WeatherAlertTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

                Greeting("lojze")
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

                            } else{

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
        painter = rememberAsyncImagePainter(R.drawable.loading),
        contentDescription = "Loading",
        modifier = modifier.size(50.dp,50.dp)
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
