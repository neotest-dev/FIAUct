package com.neotestdev.fiauct.data.remote

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseModule {
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}
