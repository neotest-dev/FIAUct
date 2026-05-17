package com.neotestdev.fiauct.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neotestdev.fiauct.data.model.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.URL

class CourseRepository {

    private val gson = Gson()
    private val courseListType = object : TypeToken<List<Course>>() {}.type

    // 🔥 IMPORTANTE: URL correcta (sin refs/heads)
    private val githubUrl =
        "https://neotest-dev.github.io/fiadata/courses.json"

    // 📦 Carga local optimizada (Streaming)
    suspend fun loadCoursesFromAssets(context: android.content.Context): List<Course> {
        return withContext(Dispatchers.IO) {
            try {
                context.assets.open("courses.json").use { inputStream ->
                    val reader = InputStreamReader(inputStream)
                    gson.fromJson<List<Course>>(reader, courseListType) ?: emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    // 🌐 Carga desde GitHub optimizada (Streaming)
    suspend fun fetchCoursesFromNetwork(): List<Course> {
        return withContext(Dispatchers.IO) {
            try {
                val urlWithCacheBuster = "$githubUrl?t=${System.currentTimeMillis()}"
                val connection = URL(urlWithCacheBuster).openConnection()
                
                connection.getInputStream().use { inputStream ->
                    val reader = InputStreamReader(inputStream)
                    gson.fromJson<List<Course>>(reader, courseListType) ?: emptyList()
                }
            } catch (e: Exception) {
                println("ERROR NETWORK: ${e.message}")
                emptyList()
            }
        }
    }
}