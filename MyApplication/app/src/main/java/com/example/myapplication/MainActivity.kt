package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.welcome_message), color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
                )
            },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF6200EE)
            ) {
                Text(
                    stringResource(id = R.string.bottom_bar_text),
                    modifier = Modifier.padding(16.dp),
                    color = Color.Black
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, context.getString(R.string.button_clicked), Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(id = R.string.clickety_click))
                }
            }
            ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF17E8B0))
                    .padding(innerPadding)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(stringResource(id = R.string.welcome_message), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                InfoCard(
                    title = stringResource(id = R.string.cardTitle1),
                    description = stringResource(id = R.string.cardText1),
                    backgroundColor = Color(0xFFE0E0E0)
                )

                Image(
                    painter = painterResource(id = R.drawable.frost),
                    contentDescription = stringResource(id = R.string.frost_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(40f / 9f),
                    contentScale = ContentScale.Crop
                )

                InfoCard(
                    title = stringResource(id = R.string.cardTitle2),
                    description = stringResource(id = R.string.cardText2)
                )
                Text(
                    stringResource(id = R.string.goodbye_message), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    Toast.makeText(context, context.getString(R.string.button_clicked), Toast.LENGTH_SHORT).show()
                }) {
                    Text(stringResource(id = R.string.click_me))
                }
            }
        }
}

@Composable
fun Login(onLoginSuccess: () -> Unit = {}) {
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(id = R.string.login_message), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it},
            label = { Text(text = stringResource(id = R.string.username))},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {password = it},
            label = { Text(text = stringResource(id = R.string.password))},
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    Toast.makeText(context, context.getString(R.string.logging_in), Toast.LENGTH_SHORT).show()
                    onLoginSuccess()
                } else {
                    Toast.makeText(context, context.getString(R.string.login_error), Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(stringResource(id = R.string.login))
            }
            Button(onClick = {
                username = ""
                password = ""
                Toast.makeText(context, context.getString(R.string.form_reset), Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(id = R.string.cancel))
            }
        }

    }
}

@Composable
fun MainApp() {
    var isLoggedIn by remember {mutableStateOf(false)}

    if(isLoggedIn) {
        MyScreen()
    } else {
        Login(onLoginSuccess = { isLoggedIn = true})
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
    }
}