package com.example.hrapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hrapp.presentation.auth.LoginScreen
import com.example.hrapp.presentation.admin.dashboard.AdminDashboardScreen
import com.example.hrapp.presentation.admin.employee.EmployeeListScreen
import com.example.hrapp.presentation.admin.employee.EmployeeDetailScreen
import com.example.hrapp.presentation.admin.employee.AddEditEmployeeScreen
import com.example.hrapp.presentation.admin.department.DepartmentListScreen
import com.example.hrapp.presentation.admin.department.AddEditDepartmentScreen
import com.example.hrapp.presentation.admin.attendance.AttendanceListScreen
import com.example.hrapp.presentation.admin.attendance.ManualCheckInScreen
import com.example.hrapp.presentation.admin.employee.RegisterFaceScreen
import com.example.hrapp.presentation.admin.salary.SalaryListScreen
import com.example.hrapp.presentation.admin.salary.SalaryDetailScreen
import com.example.hrapp.presentation.admin.salary.AdvanceSalaryScreen
import com.example.hrapp.presentation.admin.salary.AdvanceListScreen
import com.example.hrapp.presentation.employee.dashboard.EmployeeDashboardScreen
import com.example.hrapp.presentation.employee.checkin.FaceCheckInScreen
import com.example.hrapp.presentation.employee.salary.MySalaryScreen
import com.example.hrapp.presentation.employee.attendance.MyAttendanceScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(navController = navController)
        }
        composable(Screen.EmployeeList.route) {
            EmployeeListScreen(navController = navController)
        }
        composable(
            route = Screen.EmployeeDetail.route,
            arguments = listOf(navArgument("employeeId") { type = NavType.IntType })
        ) {
            EmployeeDetailScreen(navController = navController)
        }
        composable(
            route = Screen.AddEditEmployee.route,
            arguments = listOf(navArgument("employeeId") {
                type = NavType.IntType; defaultValue = -1
            })
        ) {
            AddEditEmployeeScreen(navController = navController)
        }
        composable(Screen.DepartmentList.route) {
            DepartmentListScreen(navController = navController)
        }
        composable(
            route = Screen.AddEditDepartment.route,
            arguments = listOf(navArgument("departmentId") {
                type = NavType.IntType; defaultValue = -1
            })
        ) {
            AddEditDepartmentScreen(navController = navController)
        }
        composable(Screen.AttendanceList.route) {
            AttendanceListScreen(navController = navController)
        }
        composable(
            route = Screen.ManualCheckIn.route,
            arguments = listOf(navArgument("employeeId") { type = NavType.IntType })
        ) {
            ManualCheckInScreen(navController = navController)
        }
        composable(Screen.SalaryList.route) {
            SalaryListScreen(navController = navController)
        }
        composable(
            route = Screen.SalaryDetail.route,
            arguments = listOf(navArgument("salaryId") { type = NavType.IntType })
        ) {
            SalaryDetailScreen(navController = navController)
        }
        composable(
            route = Screen.AdvanceSalary.route,
            arguments = listOf(navArgument("employeeId") { type = NavType.IntType })
        ) {
            AdvanceSalaryScreen(navController = navController)
        }
        composable(Screen.AdvanceList.route) {
            AdvanceListScreen(navController = navController)
        }
        composable(Screen.EmployeeDashboard.route) {
            EmployeeDashboardScreen(navController = navController)
        }
        composable(Screen.FaceCheckIn.route) {
            FaceCheckInScreen(navController = navController)
        }
        composable(Screen.MySalary.route) {
            MySalaryScreen(navController = navController)
        }
        composable(Screen.MyAttendance.route) {
            MyAttendanceScreen(navController = navController)
        }
        composable(
            route = Screen.RegisterFace.route,
            arguments = listOf(navArgument("employeeId") { type = NavType.IntType })
        ) {
            RegisterFaceScreen(navController = navController)
        }
    }
}