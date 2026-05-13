package com.neotestdev.fiauct.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neotestdev.fiauct.data.model.Course
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class CourseRepository {

    // 🔥 IMPORTANTE: URL correcta (sin refs/heads)
    private val githubUrl =
        "https://neotest-dev.github.io/fiadata/courses.json"

    // 📦 Carga local (backup)
    fun loadCoursesFromAssets(context: android.content.Context): List<Course> {
        return try {
            val jsonString = context.assets
                .open("courses.json")
                .bufferedReader()
                .use { it.readText() }

            val type = object : TypeToken<List<Course>>() {}.type
            Gson().fromJson(jsonString, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 🌐 Carga desde GitHub (sin caché fuerte)
    suspend fun fetchCoursesFromNetwork(): List<Course> {
        return withContext(Dispatchers.IO) {
            try {
                // 🔥 anti-cache (truco importante)
                val urlWithCacheBuster =
                    "$githubUrl?t=${System.currentTimeMillis()}"

                val connection = URL(urlWithCacheBuster).openConnection()
                connection.setRequestProperty("Cache-Control", "no-cache")
                connection.setRequestProperty("Pragma", "no-cache")
                connection.setRequestProperty("Expires", "0")

                val jsonString =
                    connection.getInputStream().bufferedReader().use { it.readText() }

                val type = object : TypeToken<List<Course>>() {}.type
                Gson().fromJson(jsonString, type)

            } catch (e: Exception) {
                println("ERROR NETWORK: ${e.message}")
                emptyList()
            }
        }
    }
}