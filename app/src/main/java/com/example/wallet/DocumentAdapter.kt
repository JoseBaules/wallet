package com.example.wallet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DocumentAdapter(private val documents: List<Document>) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val documentImage: ImageView = itemView.findViewById(R.id.documentImage)
        val documentTitle: TextView = itemView.findViewById(R.id.documentTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.document_item, parent, false)
        return DocumentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documents[position]
        holder.documentImage.setImageResource(document.imageResId)
        holder.documentTitle.text = document.title
    }

    override fun getItemCount() = documents.size
}
