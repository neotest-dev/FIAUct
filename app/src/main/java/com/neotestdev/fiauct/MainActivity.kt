package com.neotestdev.fiauct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.neotestdev.fiauct.data.repository.CourseRepository
import com.neotestdev.fiauct.ui.navigation.AppNavigation
import com.neotestdev.fiauct.ui.theme.FIAUctTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = CourseRepository()
        val courses = repository.loadCourses(this)

        setContent {
            FIAUctTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(
                        allCourses = courses,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
