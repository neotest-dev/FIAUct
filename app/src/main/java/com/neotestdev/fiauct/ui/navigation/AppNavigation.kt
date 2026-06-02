package com.neotestdev.fiauct.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.ui.screens.*

sealed class Screen(val route: String, val title: String) {
    object Programs : Screen("programs", "FIA - UCT (neotest-dev)")
    object Modalities : Screen("modalities/{program}", "Modalidad")
    object Cycles : Screen("cycles/{program}/{modality}", "Ciclos")
    object Courses : Screen("courses/{program}/{modality}/{cycle}", "Cursos")
    object CourseDetail : Screen("courseDetail/{courseName}/{docente}/{modality}/{cycle}", "Detalle del Curso")
    object CourseForm : Screen("courseForm?codigo={codigo}", "Gestionar curso")
    object Login : Screen("login", "Acceso admin")
}

private fun cycleOrder(cycle: String): Int {
    return when (cycle.trim().uppercase()) {
        "I" -> 1
        "II" -> 2
        "III" -> 3
        "IV" -> 4
        "V" -> 5
        "VI" -> 6
        "VII" -> 7
        "VIII" -> 8
        "IX" -> 9
        "X" -> 10
        else -> Int.MAX_VALUE
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    allCourses: List<Course>,
    isAdmin: Boolean,
    isAuthLoading: Boolean,
    authError: String?,
    onSignIn: (String, String) -> Unit,
    onSignOut: () -> Unit,
    onSaveCourse: (Course) -> Unit,
    onDeleteCourse: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = remember(navBackStackEntry) { navBackStackEntry?.destination?.route }

    Scaffold(
        floatingActionButton = {
            if (isAdmin && currentRoute?.startsWith("courses/") == true) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("courseForm")
                    },
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = when {
                            currentRoute?.startsWith("programs") == true -> Screen.Programs.title
                            currentRoute?.startsWith("modalities") == true -> Screen.Modalities.title
                            currentRoute?.startsWith("cycles") == true -> Screen.Cycles.title
                            currentRoute?.startsWith("courses") == true -> Screen.Courses.title
                            currentRoute?.startsWith("courseDetail") == true -> Screen.CourseDetail.title
                            currentRoute?.startsWith("courseForm") == true -> Screen.CourseForm.title
                            currentRoute?.startsWith("login") == true -> Screen.Login.title
                            else -> "FIA UCT"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isAdmin) {
                            onSignOut()
                        } else {
                            navController.navigate(Screen.Login.route)
                        }
                    }) {
                        Icon(
                            imageVector = if (isAdmin) Icons.AutoMirrored.Filled.Logout else Icons.Default.Person,
                            contentDescription = if (isAdmin) "Cerrar sesion" else "Iniciar sesion"
                        )
                    }
                    IconButton(onClick = { 
                        navController.navigate(Screen.Programs.route) {
                            popUpTo(Screen.Programs.route) { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.Home, contentDescription = "Inicio")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Programs.route,
            modifier = modifier.padding(padding),
            enterTransition = { fadeIn(animationSpec = tween(200)) },
            exitTransition = { fadeOut(animationSpec = tween(200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(200)) },
            popExitTransition = { fadeOut(animationSpec = tween(200)) }
        ) {
            composable(Screen.Programs.route) {
                val programs = allCourses.map { it.programa }.distinct()
                ProgramScreen(programs) { program ->
                    navController.navigate("modalities/$program")
                }
            }

            composable(
                route = Screen.Modalities.route,
                arguments = listOf(navArgument("program") { type = NavType.StringType })
            ) { backStackEntry ->
                val program = backStackEntry.arguments?.getString("program") ?: ""
                val modalities = allCourses.filter { it.programa == program }.map { it.modalidad }.distinct()
                ModalityScreen(program, modalities) { modality ->
                    navController.navigate("cycles/$program/$modality")
                }
            }

            composable(
                route = Screen.Cycles.route,
                arguments = listOf(
                    navArgument("program") { type = NavType.StringType },
                    navArgument("modality") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val program = backStackEntry.arguments?.getString("program") ?: ""
                val modality = backStackEntry.arguments?.getString("modality") ?: ""
                val cycles = allCourses.filter { it.programa == program && it.modalidad == modality }
                    .map { it.ciclo }
                    .distinct()
                    .sortedBy(::cycleOrder)
                CycleScreen(program, modality, cycles) { cycle ->
                    navController.navigate("courses/$program/$modality/$cycle")
                }
            }

            composable(
                route = Screen.Courses.route,
                arguments = listOf(
                    navArgument("program") { type = NavType.StringType },
                    navArgument("modality") { type = NavType.StringType },
                    navArgument("cycle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val program = backStackEntry.arguments?.getString("program") ?: ""
                val modality = backStackEntry.arguments?.getString("modality") ?: ""
                val cycle = backStackEntry.arguments?.getString("cycle") ?: ""
                val filteredCourses = allCourses.filter { 
                    it.programa == program && it.modalidad == modality && it.ciclo == cycle 
                }
                CoursesScreen(
                    program = program,
                    modality = modality,
                    cycle = cycle,
                    courses = filteredCourses,
                    isAdmin = isAdmin,
                    onEditCourse = { courseToEdit ->
                        navController.navigate("courseForm?codigo=${courseToEdit.codigo}")
                    },
                    onDeleteCourse = { courseToDelete ->
                        onDeleteCourse(courseToDelete.codigo)
                    }
                ) { course ->
                    navController.navigate("courseDetail/${course.curso}/${course.docente}/${course.modalidad}/${course.ciclo}")
                }
            }

            composable(
                route = Screen.CourseDetail.route,
                arguments = listOf(
                    navArgument("courseName") { type = NavType.StringType },
                    navArgument("docente") { type = NavType.StringType },
                    navArgument("modality") { type = NavType.StringType },
                    navArgument("cycle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
                val docente = backStackEntry.arguments?.getString("docente") ?: ""
                val modality = backStackEntry.arguments?.getString("modality") ?: ""
                val cycle = backStackEntry.arguments?.getString("cycle") ?: ""
                
                val course = allCourses.find { 
                    it.curso == courseName && 
                    it.docente == docente && 
                    it.modalidad == modality && 
                    it.ciclo == cycle
                }
                course?.let { CourseDetailScreen(it) }
            }

            composable(
                route = Screen.CourseForm.route,
                arguments = listOf(navArgument("codigo") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) {
                val courseCode = it.arguments?.getString("codigo")
                val selectedCourse = courseCode?.let { code ->
                    allCourses.find { course -> course.codigo == code }
                }
                val programs = allCourses.map { it.programa }.distinct().sorted()
                val modalities = allCourses.map { it.modalidad }.distinct().sorted()
                val cycles = allCourses.map { it.ciclo }.distinct().sortedBy(::cycleOrder)
                CourseFormScreen(
                    course = selectedCourse,
                    programs = programs,
                    modalities = modalities,
                    cycles = cycles,
                    onSave = {
                        onSaveCourse(it)
                        navController.navigateUp()
                    }
                )
            }

            composable(Screen.Login.route) {
                LaunchedEffect(isAdmin) {
                    if (isAdmin) {
                        navController.navigateUp()
                    }
                }
                LoginScreen(
                    isLoading = isAuthLoading,
                    error = authError,
                    onSignIn = onSignIn,
                    onContinueAsGuest = { navController.navigateUp() }
                )
            }
        }
    }
}
