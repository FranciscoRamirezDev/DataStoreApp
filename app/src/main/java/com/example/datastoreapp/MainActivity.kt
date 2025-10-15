package com.example.datastoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.datastoreapp.ui.theme.DataStoreAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkModeStore = StoreDarkMode(this)
            val darkMode = darkModeStore.getDarkMode.collectAsState(initial = false)
            DataStoreAppTheme(
                darkTheme = darkMode.value
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        darkModeStore,
                        darkMode.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(darkModeStore: StoreDarkMode, darkMode: Boolean, modifier: Modifier = Modifier) {

    // variable para acceder al contexto de la app
    val context = LocalContext.current
    // variable para crear el scope de la corrutina
    val scope = rememberCoroutineScope()
    // variable para acceder a las funciones del DataStore
    val dataStore = StoreUserEmail(context)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ){
        //rememberSaveable mantiene las variables al reiniciar la activity(ejem. al girar la pantalla)
       var email by rememberSaveable { mutableStateOf("") }
       val userEmail = dataStore.getEmail.collectAsState("")


        TextField(
            value = email,
            onValueChange = {email = it},
            keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Email)
            )
        Spacer(  modifier= Modifier.height(16.dp) )
        Button(onClick = {
            scope.launch {
                dataStore.saveEmail(email)
            }
        }) {Text("Guardar Email") }
        Spacer(  modifier= Modifier.height(16.dp) )
        Text(userEmail.value.toString())

        Spacer(  modifier= Modifier.height(16.dp) )

        Button(
            onClick = {
                scope.launch {
                    if (darkMode){
                       darkModeStore.saveDarkMode(false)
                    }else{
                        darkModeStore.saveDarkMode(true)
                    }
                }
            }
        ) {
            Text("Cambiar Modo Oscuro")
        }

        Spacer(  modifier= Modifier.height(16.dp) )
        Text("Modo Ligero/Modo Oscuro")
        Switch(checked = darkMode, onCheckedChange = {isChecked ->
            scope.launch {
                darkModeStore.saveDarkMode(isChecked)
            }
        })
    }
}

