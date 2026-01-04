package com.example.weatheralert.presentation.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.example.weatheralert.R
import com.example.weatheralert.api.NetworkModule
import com.example.weatheralert.api.dataClass.UserUpdateRequest
import com.example.weatheralert.presentation.MainViewModel
import com.example.weatheralert.configs.AppConfig
import com.example.weatheralert.presentation.MyHorizontalDivider
import com.example.weatheralert.ui.theme.WeatherAlertTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("MissingPermission")
@Composable
fun MainScreen(vm: MainViewModel){
    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
    */
    val appState = vm.appState.observeAsState().value

    if(appState?.isRegistered == false) {
        RegisterButton(vm)
    } else {
        SubscriptionSettings(vm)
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
fun RegisterButton(vm: MainViewModel){
    val isLoading = remember { mutableStateOf(false) }

    Button(
        {
            isLoading.value = true
            vm.registerUser({isLoading.value = false})
        }
    ) {
        Text("Click me to register a user")
        if (isLoading.value) {
            GifImage()
        }
    }
}
@Composable
fun SubscriptionSettings(
    vm: MainViewModel,
    modifier: Modifier = Modifier,
    onSubscriptionChanged: (Boolean) -> Unit = {},
) {
    var latitude by rememberSaveable { mutableStateOf("") }
    var longitude by rememberSaveable { mutableStateOf("") }
    var intervalMinutes by rememberSaveable { mutableStateOf("") }
    var subscribed by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {

        Text(
            text = "Location",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Enable subscription",
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = subscribed,
                onCheckedChange = {
                    subscribed = it
                    onSubscriptionChanged(it)
                }
            )
        }

        OutlinedTextField(
            value = intervalMinutes,
            onValueChange = { intervalMinutes = it },
            label = { Text("Update interval (minutes)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            enabled = subscribed,
            modifier = Modifier.fillMaxWidth()
        )
        val isLoading = remember { mutableStateOf(false) }

        Button({
            isLoading.value = true
            vm.updateUser(
                { isLoading.value = false },
                req = UserUpdateRequest(
                    active = subscribed,
                    latitude = latitude.toFloat(),
                    longitude = longitude.toFloat(),
                    timeIntervalH = intervalMinutes.toInt()
                )
            )
        }){
            Text("Confirm")
        }
    }
}