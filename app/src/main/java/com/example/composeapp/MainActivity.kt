package com.example.composeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.example.composeapp.ui.theme.ComposeAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeAppTheme {
                App("Android")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App("Android")
}

@Composable
fun App(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = koinViewModel()
    val users by mainViewModel.users.collectAsState()

    LaunchedEffect(users) {
        Log.d("App12", "Users changed: ${users} users")
    }

    Scaffold(
        bottomBar = { BottomBar(navController, users) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ){
        paddingValues ->
        val adjustedPadding = PaddingValues(
            top = paddingValues.calculateTopPadding(),
            bottom = 56.dp,
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current)
        )
        NavHost(navController = navController, startDestination = "home") {
            composable("home") { HomeScreen(modifier = Modifier.padding(adjustedPadding), mainViewModel) }
            composable(
                route = "detail/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId")
                DetailScreen(modifier = Modifier.padding(adjustedPadding), userId)
            }
        }
    }
}

@Composable
fun DetailScreen(modifier: Modifier, userId: String?) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.onBackground),
    ){
        if (userId != null) {
            Text(
                text = userId
            )
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier, mainViewModel: MainViewModel) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ){
        LaunchedEffect(Unit) {
            mainViewModel.getUsers()
        }
    }
}

@Composable
fun BottomBar(navController: NavController, users: List<User>) {
    val ics = listOf(
        R.drawable.group,
        R.drawable.group,
        R.drawable.group
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                ics.forEachIndexed { index, iconRes ->
                    if (index == 1) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.TopCenter
                        ) {

                        }
                    } else {
                        Column(
                            modifier = Modifier.weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        navController.navigate(
                                            if (index == 0) "home" else "detail/123"
                                        ){
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                painter = painterResource(iconRes),
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                
                            )
                            Text("Home")
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.TopCenter)
                .offset(y = 0.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, shape = androidx.compose.foundation.shape.CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.group),
                contentDescription = "",
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}