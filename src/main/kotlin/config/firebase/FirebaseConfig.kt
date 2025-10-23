package com.kevinduran.config.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.ktor.server.application.Application
import io.ktor.server.application.log

fun Application.configureMessaging() {
    val serviceAccount =
        this::class.java.classLoader.getResourceAsStream("serviceAccountKey.json")
    if (serviceAccount == null) {
        println("Failed Messaging initialize")
        return
    }
    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()
    if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options)
        log.info("Firebase initialized")
    }
    serviceAccount.run {
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
            log.info("Firebase initialized")
        }
        close()
    }
}