package com.example.noteappanalytics.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.noteappanalytics.R
import com.example.noteappanalytics.category
import com.example.noteappanalytics.model.categoryModel
import com.example.noteappanalytics.model.noteModel
import com.example.noteappanalytics.notes
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_card.view.*
import kotlinx.android.synthetic.main.note_card.view.*

class categoryAdapter (var context: Context, var data:ArrayList<categoryModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val root = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false)
        root.categoryName.text = data[position].categoryName
        //img
        val image = FirebaseStorage.getInstance().reference
        val storageRef = image.child(data[position].image)
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            val imageUrl = uri.toString()
            Picasso.with(context).load(imageUrl).into(root.categoryImage)

            Log.d("Success", "image Success")
        }
        .addOnFailureListener { exception ->
                Log.w("Failed", "image Failed", exception)
        }
        return root
    }
}