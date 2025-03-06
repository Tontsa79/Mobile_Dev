package com.example.myapplication

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import androidx.navigation.compose.*
import androidx.navigation.compose.NavHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

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

class SensorViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val _tiltAngle = MutableStateFlow(0f)
    val tiltAngle = _tiltAngle.asStateFlow()

    init {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = it.values[0]
            val y = it.values[1]
            val tilt = Math.toDegrees(Math.atan2(y.toDouble(), x.toDouble())).toFloat()
            viewModelScope.launch {
                _tiltAngle.emit(tilt)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager.unregisterListener(this)
    }
}

@Composable
fun levelTool(sensorViewModel: SensorViewModel = viewModel()) {
    val tilt by sensorViewModel.tiltAngle.collectAsState()

    val backgroungColor = when {
        abs(tilt) < 5 -> Color.Green
        abs(tilt) < 15 -> Color.Yellow
        else -> Color.Red
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroungColor)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tilt Angle: %.2f".format(tilt), style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (abs(tilt) < 5) "Level" else "Not Level",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

class CounterViewModel: ViewModel() {
    private val _count = mutableStateOf(0)
    val count: State<Int> = _count

    fun increase() {
        _count.value++
    }
    fun reset() {
        _count.value = 0
    }
}

@Composable
fun CounterDisplay(counterViewModel: CounterViewModel) {
    val count by counterViewModel.count
    Text("Counter value: $count", style = MaterialTheme.typography.headlineMedium)
}

@Composable
fun CounterControls(counterViewModel: CounterViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = { counterViewModel.increase()}) {
            Text("Increase")
        }
        Button(onClick = { counterViewModel.reset()}) {
            Text("Reset")
        }
    }
}

class TimerViewModel : ViewModel() {
    private val _seconds = MutableStateFlow(0)
    val seconds = _seconds.asStateFlow()

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _seconds.value++
            }
        }
    }
}

class userViewModel : ViewModel() {
    private val _username = mutableStateOf("")
    val username: State<String> = _username

    fun setUsername(newUsername: String) {
        _username.value = newUsername
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(navController: NavHostController, userViewModel: userViewModel) {
    val context = LocalContext.current
    val counterViewModel : CounterViewModel = viewModel()
    val timerViewModel : TimerViewModel = viewModel()
    val sensorViewModel : SensorViewModel = viewModel()

    val secondsElapsed by timerViewModel. seconds.collectAsState()

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
                BottomNavBar(navController = navController, currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Toast.makeText(context, context.getString(R.string.button_clicked), Toast.LENGTH_SHORT).show()
            }) {
                Text(stringResource(id = R.string.clickety_click))
                }
            }
            ) { innerPadding ->
        val username by userViewModel.username
        val lifeCycle = LocalLifecycleOwner.current
        var isActive by remember { mutableStateOf(true)}

        DisposableEffect(Unit) {
            val observer = LifecycleEventObserver { _, event ->
                isActive = event == Lifecycle.Event.ON_RESUME
            }
                lifeCycle.lifecycle.addObserver(observer)
                onDispose {
                    lifeCycle.lifecycle.removeObserver(observer)
                }
        }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF17E8B0))
                    .padding(innerPadding)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if(username.isNotEmpty()) "Hello, $username!"
                    else "Please enter your name",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Spacer(modifier = Modifier.height(16.dp))

                CounterDisplay(counterViewModel)
                Spacer(modifier = Modifier.height(8.dp))
                CounterControls(counterViewModel)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Seconds passed: $secondsElapsed", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                levelTool(sensorViewModel)
                Text(stringResource(id = R.string.welcome_message), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
               /* InfoCard(
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
                )*/
                /*Text(
                    stringResource(id = R.string.goodbye_message), style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    Toast.makeText(context, context.getString(R.string.button_clicked), Toast.LENGTH_SHORT).show()
                }) {
                    Text(stringResource(id = R.string.click_me))
                }*/
            }
        }
}

@Composable
fun BottomNavBar(navController: NavHostController, currentRoute: String) {
    NavigationBar(
        containerColor = Color(0xFF6200EE),
        contentColor = Color.White
    ) {
        val items = listOf(
            BottomNavItem("home", "Home", Icons.Default.Home),
            BottomNavItem("profile", "Profile", Icons.Default.Person),
            BottomNavItem("settings", "Settings", Icons.Default.Settings)
        )

        items.forEach { item ->
            NavigationBarItem(
                icon = {Icon(item.icon, contentDescription = item.label)},
                label = {Text(item.label)},
                selected = currentRoute == item.route,
                onClick = {
                    if (item.route == "settings") {
                        val username = navController.previousBackStackEntry?.savedStateHandle
                            ?.get<String>("username") ?: "Guest"

                        navController.navigate("settings/$username") {
                            launchSingleTop = true
                            restoreState = true
                        }
                    } else {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Yellow,
                    unselectedIconColor = Color.White
                )
            )
        }
    }
}

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun HomeScreen(navController: NavHostController, snackbarHostState: SnackbarHostState, userViewModel: userViewModel) {

     val username by userViewModel.username
     val currentBackStackEntry = remember { navController.currentBackStackEntry }
     val showSnackbar = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>(
         "showSnackbar"
     ) ?: false


     LaunchedEffect(showSnackbar) {
         if (showSnackbar) {
             snackbarHostState.showSnackbar("Welcome back to Home Screen!")
             navController.previousBackStackEntry?.savedStateHandle?.set("showSnackbar", false)
         }
     }

     Scaffold(
         topBar = {
             TopAppBar(
                 title = {
                     Text(
                         stringResource(id = R.string.welcome_message),
                         color = Color.Black
                     )
                 },
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = Color(0xFF6200EE)
                 )
             )
         },
         bottomBar = {
             BottomNavBar(
                 navController = navController,
                 currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home"
             )
         }
     ) { innerPadding ->
         Column(
             modifier = Modifier.fillMaxSize()
                 .padding(innerPadding).
                 padding(16.dp),
             verticalArrangement = Arrangement.Center,
             horizontalAlignment = Alignment.CenterHorizontally
         ) {
             Text(text = "Welcome to Home Screen", style = MaterialTheme.typography.headlineMedium)
             Spacer(modifier = Modifier.height(16.dp))
             Text(
                 text = if (username.isNotEmpty()) "Hello, $username!"
                 else "Please enter your name",
                 style = MaterialTheme.typography.headlineMedium
             )
             Spacer(modifier = Modifier.height(8.dp))

             TextField(
                 value = username,
                 onValueChange = {userViewModel.setUsername(it)},
                 label = {Text("User Name")},
                 modifier = Modifier.fillMaxWidth(0.8f)
             )
         }
     }
 }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, username: String?,
                 snackbarHostState: SnackbarHostState,
                 coroutineScope: CoroutineScope, userViewModel: userViewModel) {
    val username by userViewModel.username
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.welcome_message),
                        color = Color.Black
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE)
                )
            )
        },
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route ?: "home"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Detail Screen", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Hello, ${username ?: "Guest"}!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
/*@Composable
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
}*/

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val userViewModel: userViewModel = remember { userViewModel() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", modifier = Modifier.padding(innerPadding)) {
            composable("home") { HomeScreen(navController, snackbarHostState, userViewModel) }
            composable("profile") { MyScreen(navController, userViewModel)}
            composable("settings/{username}") { backstackEntry ->
                val username = backstackEntry.arguments?.getString("username")
                DetailScreen(navController, username, snackbarHostState, coroutineScope, userViewModel)
            }
        }
    }
    //MyScreen()
    /*var isLoggedIn by remember {mutableStateOf(false)}

    if(isLoggedIn) {
        MyScreen()
    } else {
        Login(onLoginSuccess = { isLoggedIn = true})
    }*/
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
    }
}