package com.example.noteappanalytics

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.noteappanalytics.adapter.categoryAdapter
import com.example.noteappanalytics.model.categoryModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_category.*

class category : AppCompatActivity() {
    val db = Firebase.firestore
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        val category = ArrayList<categoryModel>()
        val categoryAdapter = categoryAdapter(this,category)
        lvCategory.adapter = categoryAdapter
        db.collection("categories")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    category.add(
                        categoryModel(
                            document.id.toInt(),
                            document.getString("title").toString(),
                            document.getString("image").toString(),
                        )
                    )

                    Log.d("Success", "${document.id} => ${document.data}")
                }
                categoryAdapter.notifyDataSetChanged()
                if (category.isEmpty()) {
                    progressLoadingBar.isIndeterminate = true
                    progressLoadingBar.visibility = View.VISIBLE
                } else {
                    progressLoadingBar.isIndeterminate = false
                    progressLoadingBar.visibility = View.GONE
                }

            }
            .addOnFailureListener { exception ->
                Log.w("Failed", "Error getting documents.", exception)
            }

        if (category.isEmpty()) {
            progressLoadingBar.isIndeterminate = true
            progressLoadingBar.visibility = View.VISIBLE
        } else {
            progressLoadingBar.isIndeterminate = false
            progressLoadingBar.visibility = View.GONE
        }
        lvCategory.setOnItemClickListener { parent, view, position, id ->
            // Retrieve data associated with clicked item
            val categoryId = category[position].id
             selectContent("${category[position].id}","${category[position].categoryName}","Card")
            // Pass data to next screen or activity
            val intent = Intent(this, notes::class.java)
            intent.putExtra("categoryId", categoryId)
            startActivity(intent)
        }
        analytics = Firebase.analytics
        screenView("category","home")
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
}