package com.kevinduran.infrastructure.services

import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import java.util.concurrent.Executors

object MessageSenderService {
    private val messaging = FirebaseMessaging.getInstance()
    private val executor = Executors.newSingleThreadExecutor()

    fun sendToTopic(topic: String, data: Map<String, String>) {
        val message = Message.builder()
            .putAllData(data)
            .setTopic(topic)
            .setNotification(
                Notification.builder()
                    .setTitle("Syncronize")
                    .setBody("Loading server data")
                    .build()
            )
            .setAndroidConfig(
                AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .build()
            )
            .build()

        val future = messaging.sendAsync(message)
        future.addListener({
            try {
                val messageId = future.get()
                println("Message sending successfully to topic $topic with id $messageId")
            } catch (e: Exception) {
                println("Failed to send the message")
            }
        }, executor)
    }
}