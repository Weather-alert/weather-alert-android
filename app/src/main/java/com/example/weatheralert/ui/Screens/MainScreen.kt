package com.example.weatheralert.ui.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getDrawable
import com.example.weatheralert.R
import com.example.weatheralert.api.dataClass.UserUpdateRequest
import com.example.weatheralert.ui.MainViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@SuppressLint("MissingPermission")
@Composable
fun MainScreen(vm: MainViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val appState = vm.appState.observeAsState().value

        if (appState?.isRegistered == false) {
            RegisterButton(vm)
        } else {
            SubscriptionSettings(vm)
            HorizontalDivider()
            UnregisterButton(vm)
            HorizontalDivider()
            CurrentSettings(vm)
        }
    }
}

@Composable
fun LoadingGif(modifier: Modifier = Modifier) {
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
            LoadingGif()
        }
    }
}
@Composable
fun UnregisterButton(vm: MainViewModel){
    Button({
        vm.unregisterUser()
    }){
        Text("Unregister user")
    }
}
@Composable
fun CurrentSettings(vm: MainViewModel) {
    Column(
        modifier = Modifier
            .sizeIn(minWidth = 200.dp, minHeight = 100.dp, maxWidth = 500.dp, maxHeight = 200.dp)
            .padding(16.dp)
    ) {
        val userSettings = vm.user.observeAsState().value

        Text(
            text = "Active: ${userSettings?.active}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Location: ${userSettings?.latLon?.lat}, ${userSettings?.latLon?.lon}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Time interval: ${userSettings?.timeIntervalH}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "FCM token: ${userSettings?.token}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
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
    var intervalHour by rememberSaveable { mutableStateOf("") }
    var subscribed by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .sizeIn(300.dp,100.dp,500.dp,400.dp)
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
            value = intervalHour,
            onValueChange = { intervalHour = it },
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
                    latitude = if(latitude.isEmpty()) null else latitude.toFloat(),
                    longitude = if(longitude.isEmpty()) null else longitude.toFloat(),
                    timeIntervalH = if(intervalHour.isEmpty()) null else intervalHour.toInt()
                )
            )
        }){
            Text("Confirm")

            if(isLoading.value)
                LoadingGif()
        }
    }
}