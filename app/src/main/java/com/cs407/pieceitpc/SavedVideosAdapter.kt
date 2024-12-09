package com.cs407.pieceitpc

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cs407.testyoutube.YouTubeApiService

class SavedVideosAdapter(
    private var videoList: List<YouTubeApiService.VideoItem>, savedContent: SavedContent, viewModel: UserViewModel) :
    RecyclerView.Adapter<SavedVideosAdapter.videoCardHolder>() {
    val parent = savedContent
    val viewModel = viewModel

    inner class videoCardHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var videoThumbnail: ImageView = itemView.findViewById(R.id.imageView_video)
        var videoTitle: TextView = itemView.findViewById(R.id.videoTitle)
        var videoDescription: TextView = itemView.findViewById(R.id.descriptionTextView)
    }

    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : videoCardHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_list_item, parent, false)
        return videoCardHolder(view)
    }

    override fun onBindViewHolder(holder: videoCardHolder, postion : Int){
        val currVideo = videoList[postion]
        holder.videoTitle.text = currVideo.title
        holder.videoDescription.text = currVideo.description
        Glide.with(holder.itemView.context)
            .load(currVideo.thumbnailUrl)
            .error(R.drawable.pc_thumbnail)
            .into(holder.videoThumbnail)


        //play video if clicked
        holder.itemView.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${currVideo.id}"))
            holder.itemView.context.startActivity(intent)
        }
        /**
        //save video on long hold
        holder.itemView.setOnLongClickListener{
            //TODO add code to add video to database then display to saved videos
            //points to video ID
            parent.addToSavedVideos(currVideo.id)
            true
        }**/
    }

    override fun getItemCount(): Int = videoList.size
}