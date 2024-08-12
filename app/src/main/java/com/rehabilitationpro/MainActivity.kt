// MainActivity.kt
package com.rehabilitationpro

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rehabilitationpro.screens.MainMenu
import com.rehabilitationpro.screens.menus.attendance.AttendanceMainScreen
import com.rehabilitationpro.screens.menus.attendance.QRScannerScreen
import com.rehabilitationpro.screens.menus.dashboard.DashboardMainScreen
import com.rehabilitationpro.screens.menus.messenger.MessengerMainScreen
import com.rehabilitationpro.screens.menus.notice.NoticeDetailScreen
import com.rehabilitationpro.screens.menus.notice.NoticeMainScreen
import com.rehabilitationpro.screens.menus.notice.notices
import com.rehabilitationpro.screens.menus.reservation.ReservationMainScreen
import com.rehabilitationpro.screens.menus.schedule.ScheduleMainScreen
import com.rehabilitationpro.screens.signin.SignInScreen
import com.rehabilitationpro.screens.signup.SignUpScreen
import com.rehabilitationpro.sidenavigation.SideNavigation
import com.rehabilitationpro.ui.theme.RehabPROTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RehabPROTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                AppContent(navController = navController, drawerState = drawerState)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppContent(navController: NavHostController, drawerState: DrawerState) {
    SideNavigation(navController = navController, drawerState = drawerState) {
        AppNavHost(navController = navController, drawerState = drawerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController, drawerState: DrawerState) {
    NavHost(navController = navController, startDestination = Screen.Onboarding.route) {
        composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
        composable(Screen.SignIn.route) { SignInScreen(navController) }
        composable(Screen.SignUp.route) { SignUpScreen(navController) }

        // Main menu and general screens
        composable(Screen.MainMenu.route) { MainMenu(navController, drawerState) }

        // Use Notice sealed class for notice screens
        composable(Screen.NoticeScreen.Main.route) { NoticeMainScreen(navController, drawerState) }
        composable(
            route = Screen.NoticeScreen.Detail.route,
            arguments = listOf(
                navArgument("id") { type = androidx.navigation.NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: "No ID"

            // Find the notice with the given ID
            val notice = notices.find { it.id == id }

            // Pass arguments to NoticeDetailScreen
            notice?.let {
                NoticeDetailScreen(
                    navController = navController,
                    title = notice.title,
                    timestamp = notice.timestamp,
                    description = notice.description
                )
            }
        }

        composable(Screen.AttendanceScreen.Main.route) { AttendanceMainScreen(navController, drawerState) }
        composable(Screen.AttendanceScreen.QR.route) { QRScannerScreen(navController) }

        composable(Screen.ReservationScreen.Main.route) { ReservationMainScreen(navController, drawerState) }
        composable(Screen.ScheduleScreen.Main.route) { ScheduleMainScreen(navController, drawerState) }
        composable(Screen.DashboardScreen.Main.route) { DashboardMainScreen(navController, drawerState) }
        composable(Screen.MessengerScreen.Main.route) { MessengerMainScreen(navController, drawerState) }
    }
}
