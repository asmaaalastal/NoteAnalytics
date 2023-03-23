package com.example.noteappanalytics

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.noteappanalytics.adapter.noteAdapter
import com.example.noteappanalytics.model.categoryModel
import com.example.noteappanalytics.model.noteModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_notes.*

class notes : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var analytics: FirebaseAnalytics
    var screenTimeAnalytics = screenTimeAnalytics()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        var categoryId = intent.getIntExtra("categoryId", 0)
        val notes = ArrayList<noteModel>()
        val noteAdapter = noteAdapter(this@notes,notes)
        lvNotes.adapter = noteAdapter
        if(categoryId != 0){
        db.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.getLong("category_id")?.toInt() == categoryId)
                    notes.add(
                        noteModel(
                            document.getLong("id")?.toInt(),
                            document.getLong("category_id")?.toInt(),// category id
                            document.getString("image").toString(),
                            document.getString("name").toString(),
                            document.getString("description").toString(),
                            document.getLong("word_letters")?.toInt(),
                        )
                    )
                    noteAdapter.notifyDataSetChanged()
                    if (notes.isEmpty()) {
                        progressLoadingBarNote.isIndeterminate = true
                        progressLoadingBarNote.visibility = View.VISIBLE
                    } else {
                        progressLoadingBarNote.isIndeterminate = false
                        progressLoadingBarNote.visibility = View.GONE
                      }
                    Log.d("Read Successfully", "${document.id} => ${document.data}")
                }

            }
            .addOnFailureListener { exception ->
                Log.w("Read Failed", "Error getting documents.", exception)
            }}
        if (notes.isEmpty()) {
            progressLoadingBarNote.isIndeterminate = true
            progressLoadingBarNote.visibility = View.VISIBLE
        } else {
            progressLoadingBarNote.isIndeterminate = false
            progressLoadingBarNote.visibility = View.GONE
        }

        lvNotes.setOnItemClickListener { parent, view, position, id ->
            // Retrieve data associated with clicked item
            val note = notes[position]
            selectContent("${notes[position].id}","${notes[position].name}","Card")
            // Pass data to next screen or activity
            val intent = Intent(this, detail::class.java)
            intent.putExtra("note", note)
            startActivity(intent)
        }
        analytics = Firebase.analytics
        screenView("note","note")
    }
    fun selectContent(id:String,name:String,contentType:String){
        analytics.logEvent(
            FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, id);
            param(FirebaseAnalytics.Param.ITEM_NAME, name);
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType);
        }
    }
    fun screenView(screenClass:String,screenName:String){
        analytics.logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_CLASS,screenClass)
            param(FirebaseAnalytics.Param.SCREEN_NAME,screenName)
        }
    }
    override fun onResume() {
        super.onResume()
        screenTimeAnalytics.screenTrack("note")
    }

    override fun onPause() {
        super.onPause()
        screenTimeAnalytics.screenTime()
    }
}