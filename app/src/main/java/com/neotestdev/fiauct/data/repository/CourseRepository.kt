package com.neotestdev.fiauct.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.neotestdev.fiauct.data.model.Course

class CourseRepository {

    fun loadCourses(context: Context): List<Course> {

        val jsonString = context.assets
            .open("courses.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Course>>() {}.type

        return Gson().fromJson(jsonString, type)
    }
}