package com.theo.habt

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theo.habt.notificationService.HabitScheduler
import dagger.hilt.android.AndroidEntryPoint
import com.theo.habt.presentation.AnalyticScreen.AnalyticScreen
import com.theo.habt.presentation.addnewHabitScreen.AddNewHabit
import com.theo.habt.presentation.homeScreen.HomeScreen
import com.theo.habt.presentation.navigation.NavigationRouts

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            Surface {
                val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = NavigationRouts.Home,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable<NavigationRouts.Home> {

                            HomeScreen(navigateToAddHabitScreen = {
                                navController.navigate(
                                    NavigationRouts.AddHabit
                                )
                            })
                        }
                        composable<NavigationRouts.AddHabit> {
                            AddNewHabit()
                        }
                        composable<NavigationRouts.Analytic> {
                            AnalyticScreen()
                        }

                }
            }
        }
    }
}
