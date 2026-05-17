package com.example.hrapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")

    object AdminDashboard    : Screen("admin_dashboard")
    object EmployeeList      : Screen("employee_list")
    object EmployeeDetail    : Screen("employee_detail/{employeeId}") {
        fun createRoute(id: Int) = "employee_detail/$id"
    }
    object AddEditEmployee   : Screen("add_edit_employee?employeeId={employeeId}") {
        fun createRoute(id: Int? = null) =
            if (id != null) "add_edit_employee?employeeId=$id" else "add_edit_employee"
    }
    object DepartmentList    : Screen("department_list")
    object AddEditDepartment : Screen("add_edit_department?departmentId={departmentId}") {
        fun createRoute(id: Int? = null) =
            if (id != null) "add_edit_department?departmentId=$id" else "add_edit_department"
    }
    object AttendanceList    : Screen("attendance_list")
    object ManualCheckIn     : Screen("manual_checkin/{employeeId}") {
        fun createRoute(id: Int) = "manual_checkin/$id"
    }
    object SalaryList        : Screen("salary_list")
    object SalaryDetail      : Screen("salary_detail/{salaryId}") {
        fun createRoute(id: Int) = "salary_detail/$id"
    }
    object AdvanceSalary     : Screen("advance_salary/{employeeId}") {
        fun createRoute(id: Int) = "advance_salary/$id"
    }
    object AdvanceList       : Screen("advance_list")

    object EmployeeDashboard : Screen("employee_dashboard")
    object FaceCheckIn       : Screen("face_checkin")
    object MySalary          : Screen("my_salary")
    object MyAttendance      : Screen("my_attendance")

    object RegisterFace : Screen("register_face/{employeeId}") {
        fun createRoute(id: Int) = "register_face/$id"
    }
}