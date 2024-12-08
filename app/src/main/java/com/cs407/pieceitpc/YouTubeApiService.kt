package com.cs407.testyoutube

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.SearchListResponse
import com.google.api.services.youtube.model.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class YouTubeApiService {
    private val API_KEY = "AIzaSyDW6mKlHfoilQZD9d_gJ2oB4-9euXJgjgs"
    private val APPLICATION_NAME = "PieceItPC"

    private val youtube: YouTube by lazy {
        YouTube.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            HttpRequestInitializer { })
            .setApplicationName(APPLICATION_NAME)
            .build()
    }

    suspend fun searchVideos(query: String, maxResults: Long = 25): List<VideoItem> {
        return withContext(Dispatchers.IO) {
            try {
                val request = youtube.search().list(listOf("snippet"))
                    .setKey(API_KEY)
                    .setQ(query)
                    .setType(listOf("video"))
                    .setMaxResults(maxResults)

                val response: SearchListResponse = request.execute()
                parseSearchResults(response.items)
            } catch (e: GoogleJsonResponseException) {
                println("Error: ${e.details.message}")
                emptyList()
            } catch (e: Exception) {
                println("Error: ${e.message}")
                emptyList()
            }
        }
    }

    private fun parseSearchResults(searchResults: List<SearchResult>): List<VideoItem> {
        return searchResults.map { result ->
            VideoItem(
                id = result.id.videoId,
                title = result.snippet.title,
                description = result.snippet.description,
                thumbnailUrl = result.snippet.thumbnails.default.url,
                publishedAt = result.snippet.publishedAt.toString()
            )
        }
    }

    // Data class to hold video information
    data class VideoItem(
        val id: String,
        val title: String,
        val description: String,
        val thumbnailUrl: String,
        val publishedAt: String
    )
}