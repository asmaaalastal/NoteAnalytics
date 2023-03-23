package com.example.noteappanalytics

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.noteappanalytics.model.noteModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_notes.*

class detail : AppCompatActivity() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val note = intent.getParcelableExtra<noteModel>("note")
        nameNote.text = note!!.name
        descriptionNote.text = note.description
        lettersNote.text = note.word_letters.toString()
        imageNote.visibility = View.GONE
        progressLoadingBarDetail.visibility = View.VISIBLE
        val image = FirebaseStorage.getInstance().reference
        val storageRef = image.child(note.image.toString())
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            Picasso.with(this).load(imageUrl).into(imageNote)
            imageNote.visibility = View.VISIBLE
            progressLoadingBarDetail.visibility = View.GONE
            Log.d("Success", "image Success")
        }
            .addOnFailureListener { exception ->
                Log.w("Failed", "image Failed", exception)
            }
        analytics = Firebase.analytics
        screenView("detail","detail")
    }
     fun screenView(screenClass:String, screenName:String){
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS,screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME,screenName)
        }
    }
    fun customEvent(){
        analytics.logEvent("screenTime"){
            param("screenName","detail")
            param("time","MainActivity")
        }
    }
}