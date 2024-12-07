package com.cs407.pieceitpc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy


class CardAdapter(private val buildList: List<CardItem>,
                  private val homeScreen : HomeScreenFragment, viewModel: UserViewModel) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>() {
        val parent : HomeScreenFragment = homeScreen
        val viewModel = viewModel


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
        //holder.buildImage.setImageResource(currentBuild.imageResId.)
        holder.buildTitle.text = currentBuild.title
        holder.buildDescription.text = currentBuild.description
        holder.buildAuthor.text = "by ${currentBuild.author}"


        //Load the Image
        Glide.with(homeScreen)
            .load(currentBuild.imageResId)
            //.placeholder(R.drawable.pcdefault)
            .error(R.drawable.pcdefault)
            .into(holder.buildImage)

        holder.itemView.setOnClickListener {
            viewModel.setBuildVal(currentBuild.id)
            parent.findNavController().navigate(R.id.toBuildHighlights)

        }
        holder.itemView.setOnLongClickListener {
            parent.addToSaveContent(currentBuild.id)
        }
    }


    override fun getItemCount() = buildList.size
}

