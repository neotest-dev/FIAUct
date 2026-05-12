package com.neotestdev.fiauct.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.neotestdev.fiauct.data.model.Course
import com.neotestdev.fiauct.ui.screens.*

sealed class Screen(val route: String, val title: String) {
    object Programs : Screen("programs", "Carreras UCT")
    object Modalities : Screen("modalities/{program}", "Modalidad")
    object Cycles : Screen("cycles/{program}/{modality}", "Ciclos")
    object Courses : Screen("courses/{program}/{modality}/{cycle}", "Cursos")
    object CourseDetail : Screen("courseDetail/{courseName}/{docente}", "Detalle del Curso")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(allCourses: List<Course>, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
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
                            else -> "FIA UCT"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
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
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Programs.route,
            modifier = modifier.padding(padding)
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
                ModalityScreen(modalities) { modality ->
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
                    .map { it.ciclo }.distinct()
                CycleScreen(cycles) { cycle ->
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
                CoursesScreen(filteredCourses) { course ->
                    navController.navigate("courseDetail/${course.curso}/${course.docente}")
                }
            }

            composable(
                route = Screen.CourseDetail.route,
                arguments = listOf(
                    navArgument("courseName") { type = NavType.StringType },
                    navArgument("docente") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val courseName = backStackEntry.arguments?.getString("courseName") ?: ""
                val docente = backStackEntry.arguments?.getString("docente") ?: ""
                val course = allCourses.find { it.curso == courseName && it.docente == docente }
                course?.let { CourseDetailScreen(it) }
            }
        }
    }
}
