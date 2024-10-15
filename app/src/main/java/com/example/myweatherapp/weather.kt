package com.example.myweatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myweatherapp.api.NetworkResponse
import com.example.myweatherapp.api.WeatherModel
import androidx.compose.material3.Card
import coil.compose.AsyncImage
import java.time.format.DateTimeFormatter

@Composable
fun weatherPage(viewModel: WeatherViewModel){

    var city =  remember {
        mutableStateOf("")
    }

    val weatherResult = viewModel.weatherResult.observeAsState()

    Column (modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        )
    {
        Row (
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city.value,
                onValueChange = { city.value = it },
                label = {
                    Text("Search for any location")
                }
            )
            IconButton(onClick = {
                viewModel.getData(city.value)
            }) {
                Icon(imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location" )
            }
        }

        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Text(text = result.message)
            }
            is NetworkResponse.Success -> {
                WeatherDetails(result.data)
            }
            NetworkResponse.loading -> {
                CircularProgressIndicator()
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherDetails(data: WeatherModel){

    Column (modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${data.current.temp_c} °C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "http:${data.current.condition.icon}".replace("64x64","128x128"),
            contentDescription = "Condition Icon"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.size(16.dp))
        Card {
            Column (modifier = Modifier.fillMaxWidth()) {
                Row ( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    WeatherKeyValue(data.current.humidity,"humidity")
                    WeatherKeyValue(data.current.wind_kph+"km/h","Wind Speed")
                }
                Row ( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    WeatherKeyValue(data.current.uv,"UV")
                    WeatherKeyValue(data.current.precip_mm+"mm","Precipitation")
                }
                Row ( modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround){
                    WeatherKeyValue(data.location.localtime.split(" ")[1],"Local Time")
                    WeatherKeyValue(data.location.localtime.split(" ")[0],"Local Date")
                }
            }
        }
    }
}

@Composable
fun WeatherKeyValue(value:String,text:String){
    Column (modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(text= value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = text, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
}
