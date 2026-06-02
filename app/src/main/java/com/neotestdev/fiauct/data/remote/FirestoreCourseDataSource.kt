package com.neotestdev.fiauct.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.neotestdev.fiauct.data.model.Course
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class FirestoreCourseDataSource(
    private val firestore: FirebaseFirestore = FirebaseModule.firestore
) : CourseRemoteDataSource {

    private val coursesCollection = firestore.collection("courses")

    override fun observeCourses(): Flow<List<Course>> = callbackFlow {
        val registration = coursesCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val courses = snapshot?.documents
                    ?.mapNotNull { document ->
                        val programa = document.getString("programa") ?: return@mapNotNull null
                        val modalidad = document.getString("modalidad") ?: return@mapNotNull null
                        val ciclo = document.getString("ciclo") ?: return@mapNotNull null
                        val codigo = document.getString("codigo") ?: return@mapNotNull null
                        val curso = document.getString("curso") ?: return@mapNotNull null
                        val docente = document.getString("docente") ?: return@mapNotNull null
                        val modCurso = document.getString("mod-curso")
                            ?: document.getString("modCurso")
                        val horas = document.getLong("horas")?.toInt()
                        val creditos = document.getLong("creditos")?.toInt()
                        val tipoEstudio = document.getString("tipoEstudio")
                        val updatedAt = document.getTimestamp("updatedAt")?.toDate()?.time

                        Course(
                            id = document.id,
                            programa = programa,
                            modalidad = modalidad,
                            ciclo = ciclo,
                            codigo = codigo,
                            curso = curso,
                            docente = docente,
                            modCurso = modCurso,
                            horas = horas,
                            tipoEstudio = tipoEstudio,
                            creditos = creditos,
                            updatedAt = updatedAt
                        )
                    }
                    .orEmpty()
                    .sortedWith(compareByDescending<Course> { it.updatedAt ?: 0L }.thenBy { it.codigo })

                trySend(courses)
            }

        awaitClose { registration.remove() }
    }.flowOn(Dispatchers.Default)

    override suspend fun upsertCourse(course: Course) {
        val payload = hashMapOf<String, Any?>(
            "programa" to course.programa,
            "modalidad" to course.modalidad,
            "ciclo" to course.ciclo,
            "codigo" to course.codigo,
            "curso" to course.curso,
            "docente" to course.docente,
            "mod-curso" to course.modCurso,
            "horas" to course.horas,
            "tipoEstudio" to course.tipoEstudio,
            "creditos" to course.creditos,
            "updatedAt" to FieldValue.serverTimestamp()
        )

        coursesCollection.document(course.codigo).set(payload).await()
    }

    override suspend fun deleteCourse(codigo: String) {
        coursesCollection.document(codigo).delete().await()
    }
}
