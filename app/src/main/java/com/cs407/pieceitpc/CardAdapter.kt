package com.cs407.pieceitpc

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val buildList: List<CardItem>, val homeScreen : Fragment) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

        val parent = homeScreen

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buildImage: ImageView = itemView.findViewById(R.id.cardImage)
        val buildTitle: TextView = itemView.findViewById(R.id.cardTitle)
        val buildDescription: TextView = itemView.findViewById(R.id.cardDescription)
        val buildAuthor: TextView = itemView.findViewById(R.id.cardAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.build_item_cards, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentBuild = buildList[position]
        holder.buildImage.setImageResource(currentBuild.imageResId)
        holder.buildTitle.text = currentBuild.title
        holder.buildDescription.text = currentBuild.description
        holder.buildAuthor.text = "by ${currentBuild.author}"

        // Set click listener for the card
        holder.itemView.setOnClickListener {
//            val context = holder.itemView.context
//            val intent = Intent(context, ScanPart::class.java).apply {
//                putExtra("BUILD_TITLE", currentBuild.title)
//                putExtra("BUILD_DESCRIPTION", currentBuild.description)
//                putExtra("BUILD_AUTHOR", currentBuild.author)
//                putExtra("BUILD_IMAGE", currentBuild.imageResId)
//            }
//            context.startActivity(intent)
            parent. findNavController().navigate(R.id.build_highlights)

        }
    }


    override fun getItemCount() = buildList.size
}

