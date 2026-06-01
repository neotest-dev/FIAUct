package com.neotestdev.fiauct.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.neotestdev.fiauct.data.model.Course
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirestoreCourseDataSource(
    private val firestore: FirebaseFirestore = FirebaseModule.firestore
) : CourseRemoteDataSource {

    override fun observeCourses(): Flow<List<Course>> = callbackFlow {
        val registration = firestore.collection("courses")
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

                        Course(
                            programa = programa,
                            modalidad = modalidad,
                            ciclo = ciclo,
                            codigo = codigo,
                            curso = curso,
                            docente = docente,
                            modCurso = modCurso,
                            horas = horas,
                            tipoEstudio = tipoEstudio,
                            creditos = creditos
                        )
                    }
                    .orEmpty()
                    .sortedWith(compareBy({ it.programa }, { it.ciclo }, { it.codigo }))

                trySend(courses)
            }

        awaitClose { registration.remove() }
    }
}
