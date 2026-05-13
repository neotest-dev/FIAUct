package com.neotestdev.fiauct

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neotestdev.fiauct.ui.CourseUiState
import com.neotestdev.fiauct.ui.CourseViewModel
import com.neotestdev.fiauct.ui.navigation.AppNavigation
import com.neotestdev.fiauct.ui.theme.FIAUctTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: CourseViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            // Carga automática al abrir
            LaunchedEffect(Unit) {
                viewModel.loadCourses(context)
            }

            FIAUctTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (val state = uiState) {
                        is CourseUiState.Loading -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        is CourseUiState.Success -> {
                            AppNavigation(
                                allCourses = state.courses,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is CourseUiState.Error -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Error: ${state.message}")
                                    Button(
                                        onClick = { viewModel.loadCourses(context) },
                                        modifier = Modifier.padding(top = 16.dp)
                                    ) {
                                        Text("Reintentar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
