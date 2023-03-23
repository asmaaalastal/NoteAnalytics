package com.example.noteappanalytics

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class screenTimeAnalytics {
    private lateinit var analytics: FirebaseAnalytics
    private var screen: String? = null
    private var screenTime: Long? = null
    fun screenTrack(screenName: String) {
        analytics = Firebase.analytics
        screen = screenName
        screenTime = System.currentTimeMillis()
        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenName)
        }
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
        Log.e("screenTrack", "Bay Bay Screen Track Success")
    }

    fun screenTime() {
        analytics = Firebase.analytics
        if (screen != null && screenTime != null) {
            val time = System.currentTimeMillis() - screenTime!!
            storeTimeToFireStore(screen!!, time)
            screen = null
            screenTime = null
        }
        Log.e("screenTime", "Bay Bay Screen Time Success")
    }

     fun storeTimeToFireStore(name: String, time: Long) {
        val seconds = time / 1000
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("screen_time").document(name)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val existingTime = documentSnapshot.getLong("time") ?: 0
                docRef.update("time", existingTime + seconds)
            } else {
                docRef.set(mapOf("screen_name" to name, "time" to seconds))
            }
            Log.e("store time", "Success")
           }
            .addOnFailureListener {
                Log.e("store time", "Failed")
            }
     }
        }