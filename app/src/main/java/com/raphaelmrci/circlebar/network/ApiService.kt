package com.raphaelmrci.circlebar.network

import com.raphaelmrci.circlebar.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET(".")
    suspend fun getAvailableCocktails(): Response<MutableList<Cocktail>>

    //@GET("comments")
    //suspend fun getCommentsByPost(@Query("postId") postId : Int): Response<MutableList<Comment>>
}