package com.cs407.pieceitpc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cs407.testyoutube.YouTubeApiService


//TODO: change buildlist back to val ?
class CardAdapter(private var buildList: List<CardItem>, homeScreen: HomeScreenFragment?, buildInspoScreen: SavedContentOtherBuilds?, viewModel: UserViewModel) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
        val parent : AddToSavedContent = homeScreen ?: buildInspoScreen
            ?: throw IllegalArgumentException("Both screens are null")
        val viewModel = viewModel


    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val buildImage: ImageView = itemView.findViewById(R.id.cardImage)
        val buildTitle: TextView = itemView.findViewById(R.id.cardTitle)
        val buildDescription: TextView = itemView.findViewById(R.id.cardDescription)
        val buildAuthor: TextView = itemView.findViewById(R.id.cardAuthor)
        val buildSavedButton: Button = itemView.findViewById(R.id.save_build_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.build_item_cards, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val currentBuild = buildList[position]

        //might throw error
        //holder.buildImage.setImageResource(currentBuild.imageResId)
        //try
        //holder.buildImage.setImageResource(currentBuild.imageResId as Int)

        //holder.buildImage.setImageResource(currentBuild.imageResId.)

        holder.buildTitle.text = currentBuild.title
        holder.buildDescription.text = currentBuild.description
        holder.buildAuthor.text = "by ${currentBuild.author}"
        holder.buildSavedButton.setOnClickListener{
            parent.addToSavedContent(currentBuild.id)
        }

        //Load the Image
        //todo check if this is right
        //Glide.with((parent as Fragment))

        //Load the Image
        Glide.with((parent as Fragment))
            .load(currentBuild.imageResId)
            //.placeholder(R.drawable.pcdefault)
            .error(R.drawable.pcdefault)
            .into(holder.buildImage)



        // Set click listener for the card
        holder.itemView.setOnClickListener {
            viewModel.setBuildVal(currentBuild.id)
            (parent as Fragment).findNavController().navigate(R.id.toBuildHighlights)

        }
        /**
        holder.itemView.setOnLongClickListener {
            parent.addToSavedContent(currentBuild.id)
        }**/
    }

    override fun getItemCount() = buildList.size

    fun updateData(newBuilds: List<CardItem>) {
        buildList = newBuilds
        //buildList.addAll(newBuilds)
        notifyDataSetChanged()
    }
}

